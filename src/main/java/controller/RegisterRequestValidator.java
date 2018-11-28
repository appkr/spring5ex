package controller;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import spring.RegisterRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterRequestValidator implements Validator {
    private static final String emailRegExp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;

    public RegisterRequestValidator() {
        pattern = Pattern.compile(emailRegExp);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RegisterRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegisterRequest regReq = (RegisterRequest) o;
        if (regReq.getEmail() == null || regReq.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "required");
        } else {
            Matcher matcher = pattern.matcher(regReq.getEmail());
            if (matcher.matches() == false) {
                errors.rejectValue("email", "bad");
            }
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
        ValidationUtils.rejectIfEmpty(errors, "password", "required");
        ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "required");
        if (regReq.getPassword().isEmpty() == false) {
            if (regReq.isPasswordEqualToConfirmPassword() == false) {
                errors.rejectValue("confirmPassword", "nomatch");
            }
        }
    }
}
