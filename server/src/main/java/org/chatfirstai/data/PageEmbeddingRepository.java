package org.chatfirstai.data;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PageEmbeddingRepository extends JpaRepository<PageEmbedding, Integer> {

  @Query(
      value =
          "SELECT id as id, content as content, ((1 - (local_embeddings <=> CAST(:queryEmbedding AS vector))) * 100) as score FROM page_embeddings "
              + "WHERE ((1 - (local_embeddings <=> CAST(:queryEmbedding AS vector))) * 100) >= :threshold "
              + "ORDER BY local_embeddings <=> CAST(:queryEmbedding AS vector) "
              + "LIMIT :limit",
      nativeQuery = true)
  List<Map<String, Object>> localSimilaritySearch(
      @Param("queryEmbedding") float[] queryEmbedding,
      @Param("threshold") int threshold,
      @Param("limit") int limit);

  @Query(
      value =
          "SELECT id as id, content as content, ((1 - (mistral_embeddings <=> CAST(:queryEmbedding AS vector))) * 100) as score FROM page_embeddings "
              + "WHERE ((1 - (mistral_embeddings <=> CAST(:queryEmbedding AS vector))) * 100) >= :threshold "
              + "ORDER BY mistral_embeddings <=> CAST(:queryEmbedding AS vector) "
              + "LIMIT :limit",
      nativeQuery = true)
  List<Map<String, Object>> mistralSimilaritySearch(
      @Param("queryEmbedding") float[] queryEmbedding,
      @Param("threshold") int threshold,
      @Param("limit") int limit);

  @Modifying
  @Transactional
  void deleteByPageId(Integer id);

  @Modifying
  @Transactional
  @Query(
      value =
          "UPDATE page_embeddings "
              + "SET  local_embeddings = CAST(:contentEmbeddings AS vector) "
              + "WHERE id = :id",
      nativeQuery = true)
  void updateLocalEmbeddings(
      @Param("id") Integer id, @Param("contentEmbeddings") float[] contentEmbeddings);

  @Modifying
  @Transactional
  @Query(
      value =
          "UPDATE page_embeddings "
              + "SET  mistral_embeddings = CAST(:contentEmbeddings AS vector) "
              + "WHERE id = :id",
      nativeQuery = true)
  void updateMistralEmbeddings(
      @Param("id") Integer id, @Param("contentEmbeddings") float[] contentEmbeddings);
}
