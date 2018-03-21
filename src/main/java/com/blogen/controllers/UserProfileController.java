package com.blogen.controllers;

import com.blogen.commands.UserProfileCommand;
import com.blogen.services.AvatarService;
import com.blogen.services.UserService;
import com.blogen.validators.PasswordValidator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    private PasswordValidator passwordValidator;

    @Autowired
    public UserProfileController( UserService userService, AvatarService avatarService, PasswordValidator passwordValidator ) {
        this.userService = userService;
        this.avatarService = avatarService;
        this.passwordValidator = passwordValidator;
    }

    @InitBinder("user")
    protected void initBinder( WebDataBinder binder ) {
        binder.addValidators( passwordValidator );
        
    }

    @GetMapping("/profile")
    public String showProfile( Model model, Principal principal) {
        log.debug( "show user profile for user: " + principal.getName() );
        
        UserProfileCommand profileCommand = userService.getUserProfileByUserName( principal.getName() );
        profileCommand.setAvatarImages( avatarService.getAllAvatarImageNames() );

        model.addAttribute( "user", profileCommand );
        return "profile";
    }

    @PostMapping("/profile")
    public String saveProfile( @Valid @ModelAttribute("user") UserProfileCommand userProfileCommand, BindingResult bindingResult, RedirectAttributes redirectAttributes ) {
        log.debug( "saving user profile: " + userProfileCommand );
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach( objectError -> log.debug( objectError.toString() ) );
            return "profile";
        }
        userService.saveUserProfileCommand( userProfileCommand );
        redirectAttributes.addFlashAttribute( "successMessage","Settings were saved" );
        return "redirect:profile";
    }

    @PostMapping("/profile/password")
    public String savePassword( @Valid @ModelAttribute("user") UserProfileCommand command,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model ) {
        log.debug( "changing password for user id: " + command.getId() );
        log.debug( "UserProfileCommand is: " + command );
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach( objectError -> log.debug( objectError.toString() ) );
            command.setAvatarImages( avatarService.getAllAvatarImageNames() );
            model.addAttribute( "passwordError",true );
            return "profile";
        }
        userService.savePassword( command );
        redirectAttributes.addFlashAttribute( "successMessage","Password was saved" );
        return "redirect:profile";
    }
}
