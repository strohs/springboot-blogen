package com.blogen.domain;

import lombok.*;
import lombok.extern.log4j.Log4j;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Model for a blogen Post.
 * @author Cliff
 */
@Log4j
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode( of = "uuid" )
@Entity
public class Post {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String text;

    private String imageUrl;

    //a quick and hacky way to generate a unique id for a post
    private String uuid = UUID.randomUUID().toString();

    @ManyToOne
    private User user;

    @OneToOne
    private Category category;

    private LocalDateTime created = LocalDateTime.now();

    //this is the "one" side of the relationship
    @ManyToOne( cascade = CascadeType.ALL )
    private Post parent;

    //many side is the, "child side" and owner of the relationship, Usually this is the side with the foreign key.
    @OneToMany( mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Post> children = new ArrayList<>();


    public Post addChild( Post child ) {
        child.setParent( this );
        children.add( child );
        return child;
    }

    public void removeChild( Post child ) {
        //for Collections.remove() to work, need to make sure the post.equals() and hashCode() methods are working correctly
        boolean isRemoved = children.remove( child );
        if ( isRemoved ) child.setParent( null );
    }


    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", text='" + text + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", user=" + user +
                ", category=" + category +
                ", created=" + created +
                '}';
    }

    //The @OneToMany association is by definition a parent association, even if it’s a unidirectional or a
    // bidirectional one. Only the parent side of an association makes sense to cascade its entity state transitions to children.
    // ex. @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    //     private List<Phone> phones = new ArrayList<>();
    //
    //
    //On the other hand, a bidirectional @OneToMany association is much more efficient because the child entity controls the association.
}
