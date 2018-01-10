package com.blogen.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command object for transferring {@link com.blogen.domain.UserPrefs} data between the server and web-pages
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
public class UserPrefsCommand {

    Long id;

    String avatarImage;

}
