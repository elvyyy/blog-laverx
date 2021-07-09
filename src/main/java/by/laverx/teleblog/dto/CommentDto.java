package by.laverx.teleblog.dto;

import by.laverx.teleblog.domain.mysql.User;
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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentDto {
    @Null
    private Long id;

    @NotNull
    @Size(min = 1, max = 1000)
    private String message;

    @JsonProperty("created_by")
    @Null
    private UserDto user;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm")
    @Null
    private LocalDateTime createdAt;
}
