package org.chatfirstai;

import java.util.ArrayList;
import java.util.List;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.stereotype.Component;

@Component
public class MarkdownParser {
  public List<String> extractSections(String markdown) {
    Parser parser = Parser.builder().build();
    Node document = parser.parse(markdown);

    SectionVisitor visitor = new SectionVisitor();
    document.accept(visitor);

    return visitor.getSections();
  }

  static class SectionVisitor extends AbstractVisitor {

    private final List<String> sections = new ArrayList<>();
    private final TextContentRenderer renderer = TextContentRenderer.builder().build();
    private boolean insideSection = false;
    private final StringBuilder currentSection = new StringBuilder();

    @Override
    public void visit(Heading heading) {
      if (heading.getLevel() == 3) {
        if (insideSection) {
          sections.add(currentSection.toString().trim());
          currentSection.setLength(0);
        }
        insideSection = true;
      }
      currentSection.append(renderer.render(heading));
    }

    @Override
    public void visit(BulletList bulletList) {
      currentSection.append(renderer.render(bulletList));
    }

    public List<String> getSections() {
      if (insideSection && !currentSection.isEmpty()) {
        sections.add(currentSection.toString().trim());
      }
      return sections;
    }
  }
}
