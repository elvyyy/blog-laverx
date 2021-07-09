package by.laverx.teleblog.controller;

import by.laverx.teleblog.dto.CommentDto;
import by.laverx.teleblog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{articleId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getArticleComments(@PathVariable @Positive long articleId,
                                               Pageable pageable) {
        return commentService.findAll(articleId, pageable)
                .getContent();
    }

    @PostMapping("/{articleId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createArticleComment(@PathVariable long articleId,
                                           @RequestBody @Valid CommentDto commentDto,
                                           Principal principal) {
        return commentService.save(articleId, commentDto, principal);
    }

    @GetMapping("/{articleId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getArticleComment(@PathVariable @Positive long articleId,
                                        @PathVariable @Positive long commentId) {
        return commentService.find(articleId, commentId);
    }

    @DeleteMapping("/{articleId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticleComment(@PathVariable @Positive long articleId,
                                     @PathVariable @Positive long commentId) {
        commentService.delete(articleId, commentId);
    }
}
