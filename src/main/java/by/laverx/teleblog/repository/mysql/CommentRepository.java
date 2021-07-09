package by.laverx.teleblog.repository.mysql;

import by.laverx.teleblog.domain.mysql.Article;
import by.laverx.teleblog.domain.mysql.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByArticle(Article article, Pageable pageable);

    Optional<Comment> findByArticleAndId(Article article, long commentId);
}
