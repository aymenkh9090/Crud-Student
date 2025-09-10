package com.crud_app.controller;

import com.crud_app.model.ERole;
import com.crud_app.model.User;
import com.crud_app.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
       model.addAttribute("user",new User());
        return "register";
    }

   @PostMapping("/register/save")
    public String processRegistration(@ModelAttribute User user,
                                      @RequestParam("confirmedPassword")String confirmedPassword,
                                      RedirectAttributes redirectAttributes){

        if (!user.getPassword().equals(confirmedPassword)){
            redirectAttributes.addFlashAttribute("error","Passwords do not match");
            return "redirect:/register";
        }
        try {
            userService.registerUser(user, ERole.ROLE_USER);
            redirectAttributes.addFlashAttribute("success","User successfully registered");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/register";
        }




    }


    @GetMapping("/activate")
    public String activateAccount(@RequestParam String token, Model model) {
        boolean activated = userService.activateUserAccount(token);

        if (activated) {
            model.addAttribute("success", true);
            return "activation";
        } else {
            model.addAttribute("error", "Token d'activation invalide ou expiré");
            return "activation";
        }
    }




}
