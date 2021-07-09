package by.laverx.teleblog.controller;

import by.laverx.teleblog.dto.ArticleDto;
import by.laverx.teleblog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/my")
    public List<ArticleDto> getUserArticles(Principal principal, Pageable pageable) {
        return userService.findAll(principal, pageable)
                .getContent();
    }

}
