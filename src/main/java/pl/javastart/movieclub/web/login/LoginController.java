package pl.javastart.movieclub.web.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.javastart.movieclub.web.htmx.Htmx;

@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    @GetMapping(
            path = "/login",
            headers = Htmx.HEADER_HX_REQUEST
    )
    public String loginDialog() {
        return "dialogs/login-dialog::login-dialog";
    }
}
