package org.chatfirstai.data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pages")
public class Page {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pages_id_gen")
  @SequenceGenerator(name = "pages_id_gen", sequenceName = "pages_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  private String title;

  @Column(name = "content", length = Integer.MAX_VALUE)
  private String content;

  @OneToMany
  @JoinColumn(name = "page_id")
  private List<PageEmbedding> pageEmbeddings;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public List<PageEmbedding> getPageEmbeddings() {
    return pageEmbeddings;
  }

  public void setPageEmbeddings(List<PageEmbedding> pageEmbeddings) {
    this.pageEmbeddings = pageEmbeddings;
  }
}
