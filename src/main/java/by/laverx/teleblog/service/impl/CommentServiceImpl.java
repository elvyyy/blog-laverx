package by.laverx.teleblog.service.impl;

import by.laverx.teleblog.domain.mysql.Article;
import by.laverx.teleblog.domain.mysql.Comment;
import by.laverx.teleblog.dto.CommentDto;
import by.laverx.teleblog.exception.EntityNotFoundException;
import by.laverx.teleblog.exception.ErrorCode;
import by.laverx.teleblog.locale.LocaleTranslator;
import by.laverx.teleblog.repository.mysql.ArticleRepository;
import by.laverx.teleblog.repository.mysql.CommentRepository;
import by.laverx.teleblog.repository.mysql.UserRepository;
import by.laverx.teleblog.service.CommentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final LocaleTranslator localeTranslator;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentDto save(long articleId, CommentDto commentDto, Principal principal) {
        Comment comment = modelMapper.map(commentDto, Comment.class);

        userRepository.findByEmail(principal.getName())
                .ifPresent(comment::setUser);
        articleRepository.findById(articleId)
                .ifPresent(comment::setArticle);

        LocalDateTime createDate = LocalDateTime.now();
        comment.setCreatedAt(createDate);

        Comment savedComment = commentRepository.save(comment);
        return modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentDto> findAll(long articleId, Pageable pageable) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> {
                    String message = localeTranslator.toLocale("entity.article.not-found");
                    return new EntityNotFoundException(message, ErrorCode.ENTITY_NOT_FOUND.getErrorCode());
                });
        return commentRepository.findAllByArticle(article, pageable)
                .map(comment -> modelMapper.map(comment, CommentDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto find(long articleId, long commentId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> {
                    String message = localeTranslator.toLocale("entity.article.not-found");
                    return new EntityNotFoundException(message, ErrorCode.ENTITY_NOT_FOUND.getErrorCode());
                });

        return commentRepository.findByArticleAndId(article, commentId)
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .orElseThrow(() -> {
                    String message = localeTranslator.toLocale("entity.comment.not-found");
                    return new EntityNotFoundException(message, ErrorCode.ENTITY_NOT_FOUND.getErrorCode());
                });
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto find(long commentId) {
        return commentRepository.findById(commentId)
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .orElseThrow(() -> {
                    String message = localeTranslator.toLocale("entity.comment.not-found");
                    return new EntityNotFoundException(message, ErrorCode.ENTITY_NOT_FOUND.getErrorCode());
                });
    }

    @Override
    public void delete(long articleId, long commentId) {
        commentRepository.deleteById(commentId);
    }
}
