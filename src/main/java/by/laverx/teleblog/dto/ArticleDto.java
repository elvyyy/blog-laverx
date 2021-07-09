package by.laverx.teleblog.dto;

import by.laverx.teleblog.domain.mysql.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ArticleDto {
    @Null
    private Long id;
    @NotNull
    @Size(min = 3, max = 128)
    private String title;
    @NotNull
    @Size(min = 10, max = 3000)
    private String text;

    @Null
    private ArticleStatus status;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm")
    @Null
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm")
    @Null
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    @Null
    private UserDto user;

    @Size(max = 25)
    private Set<TagDto> tags = new LinkedHashSet<>();
}
