package org.chatfirstai;

import java.util.ArrayList;
import java.util.List;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.springframework.stereotype.Component;

@Component
public class MarkdownParser {
  // Method to extract sections from the AST
  public List<String> extractSections(String document) {
    // Parse the Markdown string
    Parser parser = Parser.builder().build();
    Node node = parser.parse(document);

    List<String> sections = new ArrayList<>();
    StringBuilder currentSection = new StringBuilder();
    StringBuilder currentTitle = new StringBuilder();

    // Traverse through the document node tree
    Node currentNode = node.getFirstChild();
    while (currentNode != null) {
      if (currentNode instanceof Heading) {
        // Heading marks the start of a new section
        if (!currentSection.isEmpty()) {
          sections.add(currentTitle + "\n" + currentSection);
        }
        // Reset for the new section
        currentTitle.setLength(0);
        currentSection.setLength(0);
        // Get the content of the Heading
        currentTitle.append(getTextContent(currentNode));
      } else if (currentNode instanceof Paragraph || currentNode instanceof Text) {
        // Append text content to the current section
        currentSection.append(getTextContent(currentNode)).append("\n");
      }

      // Move to the next node
      currentNode = currentNode.getNext();
    }

    // Add the last section if available
    if (!currentSection.isEmpty()) {
      sections.add(currentTitle + "\n" + currentSection);
    }

    return sections;
  }

  // Helper method to extract text content from a node
  private String getTextContent(Node node) {
    if (node instanceof Text) {
      return ((Text) node).getLiteral();
    } else if (node instanceof Heading) {
      StringBuilder headingText = new StringBuilder();
      Node headingChild = node.getFirstChild();
      while (headingChild != null) {
        if (headingChild instanceof Text) {
          headingText.append(((Text) headingChild).getLiteral());
        }
        headingChild = headingChild.getNext();
      }
      return headingText.toString();
    } else if (node instanceof Paragraph) {
      StringBuilder paragraphText = new StringBuilder();
      Node paragraphChild = node.getFirstChild();
      while (paragraphChild != null) {
        if (paragraphChild instanceof Text) {
          paragraphText.append(((Text) paragraphChild).getLiteral());
        }
        paragraphChild = paragraphChild.getNext();
      }
      return paragraphText.toString();
    }
    return "";
  }
}
