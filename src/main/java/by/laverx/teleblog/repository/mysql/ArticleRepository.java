package by.laverx.teleblog.repository.mysql;

import by.laverx.teleblog.domain.mysql.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByTitle(String title);

    Page<Article> findByUser_Id(Long userId, Pageable pageable);
}
