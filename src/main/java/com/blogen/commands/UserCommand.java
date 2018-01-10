package com.blogen.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    UserPrefsCommand userPrefs;



}
