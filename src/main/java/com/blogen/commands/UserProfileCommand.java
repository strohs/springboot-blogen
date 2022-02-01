package com.blogen.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Command object for the profile.html page
 *
 * Password validation done in {@link com.blogen.validators.PasswordValidator}
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
public class UserProfileCommand {

    //user id
    Long id;

    String userName;

    @javax.validation.constraints.NotBlank
    String firstName;

    @javax.validation.constraints.NotBlank
    String lastName;

    @javax.validation.constraints.Email
    String email;

    //validation for password/confirmPassword done by PasswordValidator class
    String password;

    String confirmPassword;

    //list of avatar image names to display on user profile page
    List<String> avatarImages;

    @javax.validation.constraints.NotBlank
    String avatarImage;
}
