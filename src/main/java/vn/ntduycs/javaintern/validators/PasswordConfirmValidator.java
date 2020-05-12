package vn.ntduycs.javaintern.validators;

import vn.ntduycs.javaintern.payloads.ShouldConfirmPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirm, ShouldConfirmPassword> {

    @Override
    public boolean isValid(ShouldConfirmPassword request, ConstraintValidatorContext context) {
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
