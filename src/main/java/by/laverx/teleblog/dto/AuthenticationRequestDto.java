package by.laverx.teleblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthenticationRequestDto {
    @NotBlank(message = "{message.email.invalid}")
    @Email(message = "{message.email.invalid}")
    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z]).{6,40}$", message = "{message.password.invalid}")
    private String password;
}
