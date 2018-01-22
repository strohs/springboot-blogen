package com.blogen.controllers;

import com.blogen.commands.UserCommand;
import com.blogen.services.AvatarService;
import com.blogen.services.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

/**
 * Controller for the user profile page
 * @author Cliff
 */
@Log4j
@Controller
public class UserProfileController {

    private UserService userService;
    private AvatarService avatarService;

    @Autowired
    public UserProfileController( UserService userService, AvatarService avatarService ) {
        this.userService = userService;
        this.avatarService = avatarService;
    }

    @GetMapping("/profile")
    public String showProfile( Model model, Principal principal) {
        log.debug( "show user profile for user: " + principal.getName() );

        UserCommand userCommand = userService.getUserByUserName( principal.getName() );
        userCommand.setAvatarImages( avatarService.getAllAvatarImageNames() );
        model.addAttribute( "user", userCommand );
        return "profile";
    }

    @PostMapping("/profile")
    public String saveProfile( @ModelAttribute UserCommand userCommand ) {
        log.debug( "saving user profile: " + userCommand );
        userService.saveUserCommand( userCommand );
        return "redirect:/profile";
    }
}
