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

    //deleting a user should delete their userPrefs
    @OneToOne(cascade = CascadeType.ALL)
    private UserPrefs userPrefs;


    public void setUserPrefs( UserPrefs userPrefs ) {
        if ( userPrefs != null ) {
            this.userPrefs = userPrefs;
            userPrefs.setUser( this );
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder( "User{" );
        sb.append( "id=" ).append( id );
        sb.append( ", firstName='" ).append( firstName ).append( '\'' );
        sb.append( ", lastName='" ).append( lastName ).append( '\'' );
        sb.append( ", email='" ).append( email ).append( '\'' );
        sb.append( ", userName='" ).append( userName ).append( '\'' );
        sb.append( ", password='" ).append( password ).append( '\'' );
        if ( userPrefs != null )
          sb.append( ", userPrefs=" ).append( userPrefs );
        else
            sb.append( ", userPrefs=null" );
        sb.append( '}' );
        return sb.toString();
    }
}
