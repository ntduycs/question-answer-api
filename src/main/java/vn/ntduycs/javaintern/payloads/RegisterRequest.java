package vn.ntduycs.javaintern.payloads;

import lombok.*;
import vn.ntduycs.javaintern.validators.PasswordConfirm;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@PasswordConfirm
public class RegisterRequest extends BaseRequest implements ShouldConfirmPassword {

    @NotBlank(message = "'email' is required but not be given")
    @Email(message = "Invalid email format", regexp = "^[A-Za-z0-9]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    private String email;

    @NotBlank(message = "'password' is required but not be given")
    @Size(min = 6, message = "'password' must contain at least 6 characters")
    private String password;

    @NotBlank(message = "'confirmPassword' is required but not be given")
    private String confirmPassword;

    @NotBlank(message = "'fullName' is required but not be given")
    private String fullName;

}
