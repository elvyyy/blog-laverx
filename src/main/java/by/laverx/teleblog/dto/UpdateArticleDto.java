package by.laverx.teleblog.dto;

import by.laverx.teleblog.domain.mysql.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateArticleDto {
    @Size(min = 3, max = 128)
    private String title;

    @Size(min = 10, max = 3000)
    private String text;

    private ArticleStatus status;

    @Size(max = 25)
    private Set<TagDto> tags = new LinkedHashSet<>();
}
