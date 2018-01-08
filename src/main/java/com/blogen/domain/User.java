package com.blogen.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Model for a user of Blogen
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String email;


    @Column(nullable = false, unique = true)
    private String userName;

    private String password;


}
