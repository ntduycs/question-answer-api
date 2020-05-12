package vn.ntduycs.javaintern.payloads;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class LoginRequest extends BaseRequest {

    @NotBlank(message = "Email is required but not be given")
    @Email(message = "Invalid email format", regexp = "^[A-Za-z0-9]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    private String email;

    @NotBlank(message = "Password is required but not be given")
    private String password;

}
