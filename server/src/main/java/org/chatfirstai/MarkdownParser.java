package org.chatfirstai;

import java.util.ArrayList;
import java.util.List;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.springframework.stereotype.Component;

@Component
public class MarkdownParser {

  public List<String> extractSections(String markdownContent) {
    List<String> sections = new ArrayList<>();

    // Initialize the CommonMark parser
    Parser parser = Parser.builder().build();
    Node document = parser.parse(markdownContent);

    // Custom visitor to walk through the document and extract level 3 headings and their following content
    document.accept(new AbstractVisitor() {
      private boolean inHeadingSection = false;
      private final StringBuilder sectionContent = new StringBuilder();

      @Override
      public void visit(Heading heading) {
        if (heading.getLevel() == 3) {
          if (inHeadingSection) {
            // If we're already in a section, complete it and add to sections list
            sections.add(sectionContent.toString().trim());
            sectionContent.setLength(0); // Reset for the next section
          }

          // Start a new section with the heading text
          inHeadingSection = true;
          StringBuilder headingText = new StringBuilder();
          Node child = heading.getFirstChild();
          while (child != null) {
            if (child instanceof Text) {
              headingText.append(((Text) child).getLiteral());
            }
            child = child.getNext();
          }
          sectionContent.append("### ").append(headingText.toString()).append("\n");
        } else if (heading.getLevel() > 3 && inHeadingSection) {
          // Finish the current section if we hit a non-level-3 heading
          sections.add(sectionContent.toString().trim());
          sectionContent.setLength(0);
          inHeadingSection = false;
        }
      }

      @Override
      public void visit(Paragraph paragraph) {
        if (inHeadingSection) {
          Node child = paragraph.getFirstChild();
          while (child != null) {
            if (child instanceof Text) {
              sectionContent.append(((Text) child).getLiteral()).append("\n");
            }
            child = child.getNext();
          }
        }
      }

      @Override
      public void visit(BulletList bulletList) {
        if (inHeadingSection) {
          sectionContent.append("- ");
          Node child = bulletList.getFirstChild();
          while (child != null) {
            if (child instanceof Text) {
              sectionContent.append(((Text) child).getLiteral()).append("\n");
            }
            child = child.getNext();
          }
        }
      }

      @Override
      public void visit(ThematicBreak thematicBreak) {
        // End the section if a horizontal line or other non-text block is encountered
        if (inHeadingSection) {
          sections.add(sectionContent.toString().trim());
          sectionContent.setLength(0);
          inHeadingSection = false;
        }
      }

      @Override
      public void visit(BlockQuote blockQuote) {
        if (inHeadingSection) {
          sectionContent.append("> ");
          Node child = blockQuote.getFirstChild();
          while (child != null) {
            if (child instanceof Text) {
              sectionContent.append(((Text) child).getLiteral()).append("\n");
            }
            child = child.getNext();
          }
        }
      }

      @Override
      public void visit(Document document) {
        // When done traversing, ensure the last section gets added
        super.visit(document);
        if (inHeadingSection && !sectionContent.isEmpty()) {
          sections.add(sectionContent.toString().trim());
        }
      }
    });

    return sections;
  }
}
