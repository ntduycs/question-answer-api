package vn.ntduycs.javaintern.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordConfirmValidator.class)
public @interface PasswordConfirm {

    String message() default "'confirmPassword' does not match 'password'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
