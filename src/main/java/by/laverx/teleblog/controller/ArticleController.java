package by.laverx.teleblog.controller;

import by.laverx.teleblog.dto.ArticleDto;
import by.laverx.teleblog.dto.UpdateArticleDto;
import by.laverx.teleblog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/articles/{articleId}")
    public ArticleDto getArticle(@PathVariable @Min(1) long articleId) {
        return articleService.find(articleId);
    }

    @PostMapping("/articles")
    public ArticleDto createArticle(@RequestBody @Valid ArticleDto articleDto, Principal principal) {
        return articleService.save(articleDto, principal);
    }

    @DeleteMapping("/articles/{articleId}")
    public void deleteArticle(@PathVariable @Min(1) long articleId) {
        articleService.delete(articleId);
    }

    @PutMapping("/articles/{articleId}")
    public ArticleDto updateArticle(@RequestBody @Valid UpdateArticleDto articleDto, @PathVariable @Min(1) long articleId) {
        return articleService.update(articleDto, articleId);
    }

    @GetMapping("/articles")
    public Page<ArticleDto> getArticles(Pageable pageable) {
        throw new UnsupportedOperationException();
    }
}
