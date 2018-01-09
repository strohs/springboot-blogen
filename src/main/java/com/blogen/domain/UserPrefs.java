package com.blogen.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Domain object that will hold user specific preferences for the Blogen web-site
 *
 * User: Cliff
 */
@Data
@NoArgsConstructor
@Entity
public class UserPrefs {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    Long id;

    @OneToOne
    User user;

    //avatar image file name
    String avatarImage;
}
