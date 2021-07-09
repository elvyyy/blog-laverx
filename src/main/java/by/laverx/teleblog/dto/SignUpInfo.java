package by.laverx.teleblog.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SignUpInfo {
    @NotBlank(message = "{message.first-name.empty}")
    @Size(min = 1, max = 60, message = "{message.size.name}")
    private String firstName;

    @NotBlank(message = "{message.last-name.empty}")
    @Size(min = 1, max = 60, message = "{message.size.name}")
    private String lastName;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z]).{6,40}$", message = "{message.password.invalid}")
    private String password;

    @NotBlank(message = "{message.email.invalid}")
    @Email(message = "{message.email.invalid}")
    private String email;
}
