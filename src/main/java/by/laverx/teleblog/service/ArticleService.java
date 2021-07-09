package by.laverx.teleblog.service;

import by.laverx.teleblog.domain.mysql.Article;
import by.laverx.teleblog.dto.ArticleDto;
import by.laverx.teleblog.dto.UpdateArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;
import java.util.Optional;

public interface ArticleService {
    @PreAuthorize("permitAll()")
    ArticleDto find(long articleId);

    @PreAuthorize("hasAuthority('USER') and @userSecurity.hasUserId(authentication, #articleId)")
    void delete(long articleId);

    @PreAuthorize("hasAuthority('USER')")
    ArticleDto save(ArticleDto articleDto, Principal principal);

    @PreAuthorize("hasAuthority('USER') and @userSecurity.hasArticle(authentication, #articleId)")
    ArticleDto update(UpdateArticleDto articleDto, long articleId);
}
