package com.blogen.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for {@link com.blogen.domain.UserPrefs}
 *
 * User: Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrefsDTO {

    private Long id;

    private String avatarImage;
}
