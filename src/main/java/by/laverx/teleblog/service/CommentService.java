package by.laverx.teleblog.service;

import by.laverx.teleblog.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;

public interface CommentService {
    @PreAuthorize("hasAuthority('USER')")
    CommentDto save(long articleId, CommentDto commentDto, Principal principal);

    @PreAuthorize("permitAll()")
    Page<CommentDto> findAll(long articleId, Pageable pageable);

    @PreAuthorize("permitAll()")
    CommentDto find(long articleId, long commentId);

    @PreAuthorize("permitAll()")
    CommentDto find(long commentId);

    @PreAuthorize(
            "hasAuthority('USER') and (@userSecurity.hasArticle(authentication, #commentId) or @userSecurity.hasComment(authentication, #commentId))"
    )
    void delete(long articleId, long commentId);
}
