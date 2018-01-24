package com.blogen.commands;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

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

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @Email
    String email;

    //validation for password done by PasswordValidator class
    String password;

    String confirmPassword;

    //list of avatar image names to display on user profile page
    List<String> avatarImages;

    //set to true if password validation errors
    boolean isPasswordError = false;

    @NotBlank
    String avatarImage;
}
