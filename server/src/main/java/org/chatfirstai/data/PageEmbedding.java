package org.chatfirstai.data;

import jakarta.persistence.*;

@Entity
@Table(name = "page_embeddings")
public class PageEmbedding {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pageEmbeddings_id_gen")
  @SequenceGenerator(
      name = "pageEmbeddings_id_gen",
      sequenceName = "pageEmbeddings_id_seq",
      allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "content", length = Integer.MAX_VALUE)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "page_id")
  private Page page;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page pages) {
    this.page = pages;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
