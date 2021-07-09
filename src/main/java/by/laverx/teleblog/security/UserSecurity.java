package by.laverx.teleblog.security;

import by.laverx.teleblog.dto.ArticleDto;
import by.laverx.teleblog.dto.CommentDto;
import by.laverx.teleblog.dto.UserDto;
import by.laverx.teleblog.exception.EntityNotFoundException;
import by.laverx.teleblog.service.ArticleService;
import by.laverx.teleblog.service.CommentService;
import by.laverx.teleblog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@AllArgsConstructor
public class UserSecurity {
    private final UserService userService;
    private final ArticleService articleService;
    private final CommentService commentService;

    public boolean hasUserId(Authentication authentication, Long userId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        return userService.findByEmail(email)
                .map(UserDto::getId)
                .map(userId::equals)
                .orElse(false);
    }

    public boolean hasArticle(Authentication authentication, Long articleId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        ArticleDto articleDto = null;
        try {
            articleDto = articleService.find(articleId);
        } catch (EntityNotFoundException e) {
            return false;
        }
        return articleDto.getUser().getEmail().equals(email);
    }

    public boolean hasComment(Authentication authentication, Long commentId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        CommentDto commentDto = null;
        try {
            commentDto = commentService.find(commentId);
        } catch (EntityNotFoundException e) {
            return false;
        }
        return commentDto.getUser().getEmail().equals(email);
    }

}
