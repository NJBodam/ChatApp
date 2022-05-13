package decagon.week8.chatapp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("invalid", null);
        model.addAttribute("newRegistration", null);
        return "login";
    }


    @PostMapping("/login")
    public String login(HttpSession httpSession, User user, Model model) {
        User user1 = userService.getUserByEmail(user.getEmail());
        if (user1 == null) {
            model.addAttribute("invalid", "User does not exist. Check login details or register.");
            return "login";
        }
        user1 = userService.getUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if (user1 == null) {
            model.addAttribute("invalid", "Incorrect password");
            return "redirect:/login";
        }
        httpSession.setAttribute("user", user1);
        return "index";
    }


    @GetMapping("/register")
    public String registerUser(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("invalid", null);
        model.addAttribute("newRegistration", null);
        return "register";
    }

    @PostMapping("/register")
    public String successfullyRegisterUser(@ModelAttribute("user") User user){
        userService.addUser(user);
        return "redirect:/login";
    }


    @GetMapping("/logout")
    public String loggingOut(Model model, HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        model.addAttribute("user", new User());
        model.addAttribute("invalid", null);
        return "redirect:/login";
    }
}

