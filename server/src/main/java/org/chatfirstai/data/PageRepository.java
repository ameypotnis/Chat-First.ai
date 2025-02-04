package org.chatfirstai.data;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {

  @Query(
      value =
          "SELECT p.* FROM pages p WHERE \"updatedAt\"::TIMESTAMPTZ >= NOW() - INTERVAL '10 minutes'",
      nativeQuery = true)
  List<Map<String, Object>> findPagesUpdatedWithinLast10Minutes();
}
