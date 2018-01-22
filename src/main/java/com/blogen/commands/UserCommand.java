package com.blogen.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Command object for transferring {@link com.blogen.domain.User} data between the server and web-pages
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
public class UserCommand {

    Long id;

    String userName;

    String firstName;

    String lastName;

    String email;

    String password;

    String confirmPassword;

    //list of avatar image names to display on user profile page
    List<String> avatarImages;

    UserPrefsCommand userPrefs;



}
