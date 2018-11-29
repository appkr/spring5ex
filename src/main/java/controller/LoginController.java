package controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.AuthInfo;
import spring.AuthService;
import spring.WrongIdPasswordException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    private AuthService authService;

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String form(LoginCommand loginCommand,
                       @CookieValue(value = "REMEMBER", required = false)Cookie rCookie) {
        if (rCookie != null) {
            // 커맨드 객체에 미리 쿠키에서 읽은 값을 셋팅해서 뷰 렌더링
            loginCommand.setEmail(rCookie.getValue());
            loginCommand.setRememberEmail(true);
        }

        return "login/loginForm";
    }

    @PostMapping
    public String submit(LoginCommand loginCommand, Errors errors, HttpSession session, HttpServletResponse response) {
        (new LoginCommandValidator()).validate(loginCommand, errors);
        if (errors.hasErrors()) {
            return "login/loginForm";
        }

        try {
            AuthInfo authInfo = authService.authenticate(loginCommand.getEmail(), loginCommand.getPassword());
            session.setAttribute("authInfo", authInfo);

            Cookie rememberCookie = new Cookie("REMEMBER", loginCommand.getEmail());
            rememberCookie.setPath("/");
            if (loginCommand.isRememberEmail()) {
                // 체크박스가 켜져 있으면, Set-Cookie 응답 헤더 전송
                rememberCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
            } else {
                // 체크박스가 꺼져 있으면, 1970-01-01에 만료되는 Set-Cookie 응답 헤더 전송
                rememberCookie.setMaxAge(0);
            }
            response.addCookie(rememberCookie);
        } catch (WrongIdPasswordException e) {
            errors.reject("idPasswordNotMatching");
            return "login/loginForm";
        }

        return "login/loginSuccess";
    }
}
