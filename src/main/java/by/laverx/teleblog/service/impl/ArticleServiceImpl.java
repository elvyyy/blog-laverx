package by.laverx.teleblog.service.impl;

import by.laverx.teleblog.domain.mysql.Article;
import by.laverx.teleblog.domain.mysql.ArticleStatus;
import by.laverx.teleblog.dto.ArticleDto;
import by.laverx.teleblog.dto.UpdateArticleDto;
import by.laverx.teleblog.exception.EntityExistsException;
import by.laverx.teleblog.exception.EntityNotFoundException;
import by.laverx.teleblog.exception.ErrorCode;
import by.laverx.teleblog.locale.LocaleTranslator;
import by.laverx.teleblog.repository.mysql.ArticleRepository;
import by.laverx.teleblog.repository.mysql.UserRepository;
import by.laverx.teleblog.service.ArticleService;
import by.laverx.teleblog.util.CopyUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {
    private final LocaleTranslator localeTranslator;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public ArticleDto find(long articleId) {
        return articleRepository.findById(articleId)
                .map(article -> modelMapper.map(article, ArticleDto.class))
                .orElseThrow(() -> {
                    String message = localeTranslator.toLocale("entity.article.not-exist");
                    return new EntityNotFoundException(message, ErrorCode.ENTITY_NOT_FOUND.getErrorCode());
                });
    }

    @Override
    @Transactional
    public void delete(long articleId) {
        articleRepository.findById(articleId)
                .ifPresent(articleRepository::delete);
    }

    @Override
    @Transactional
    public ArticleDto save(ArticleDto articleDto, Principal principal) {
        Article article = modelMapper.map(articleDto, Article.class);
        if (articleRepository.existsByTitle(article.getTitle())) {
            String message = localeTranslator.toLocale("entity.article.exists");
            throw new EntityExistsException(message, ErrorCode.ENTITY_ALREADY_EXISTS.getErrorCode());
        }

        userRepository.findByEmail(principal.getName())
                .ifPresent(article::setUser);

        LocalDateTime currentTime = LocalDateTime.now();
        article.setCreatedAt(currentTime);
        article.setUpdatedAt(currentTime);
        article.setStatus(ArticleStatus.PUBLIC);

        Article savedArticle = articleRepository.save(article);
        return modelMapper.map(savedArticle, ArticleDto.class);
    }

    @Override
    @Transactional
    public ArticleDto update(UpdateArticleDto articleDto, long articleId) {
        Article article = modelMapper.map(articleDto, Article.class);
        if (articleRepository.existsByTitle(article.getTitle())) {
            String message = localeTranslator.toLocale("entity.article.exists");
            throw new EntityExistsException(message, ErrorCode.ENTITY_ALREADY_EXISTS.getErrorCode());
        }

        return articleRepository.findById(articleId)
                .map(articleToUpdate -> {
                    CopyUtils.copyNonNullProperties(article, articleToUpdate);
                    articleToUpdate.setUpdatedAt(LocalDateTime.now());
                    return modelMapper.map(articleToUpdate, ArticleDto.class);
                })
                .orElseThrow(() -> {
                    String message = localeTranslator.toLocale("entity.article.not-found");
                    return new EntityNotFoundException(message, ErrorCode.ENTITY_NOT_FOUND.getErrorCode());
                });
    }
}
