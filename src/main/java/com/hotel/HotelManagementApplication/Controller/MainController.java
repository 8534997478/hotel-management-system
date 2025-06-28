package com.hotel.HotelManagementApplication.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/default")
    public String defaultAfterLogin(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if ("ROLE_ADMIN".equals(role)) {
            return "redirect:/admin/dashboard";
        } else if ("ROLE_USER".equals(role)) {
            return "redirect:/user/dashboard";
        }

        return "redirect:/login?error=unauthorized";
    }
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "login");
        return "login";
    }
    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("title", "logout");
        return "login";
    }


}
