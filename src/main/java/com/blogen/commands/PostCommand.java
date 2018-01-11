package com.blogen.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Command object for transferring {@link com.blogen.domain.Post} data between the server and web-pages
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
public class PostCommand {

    Long id;

    //parentId will be null if this is a Parent Post, otherwise it will be set to the actual parent ID
    Long parentId;

    String title;

    String text;

    String imageUrl;

    Long categoryId;

    //this should already be formatted by PostCommandMapper
    String created;

    //the user id of user making the posting
    Long userId;

    //the username of the user making the posting
    String userName;

    List<PostCommand> children = new ArrayList<>();

    //holds the current page of Post data being viewed on the page. Default to page 1
    //TODO decide how to map this and on what object. May need new command object.
    //int currentPage = 1;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", title=" + title +
                ", text='" + text + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userId=" + getUserId() +
                ", userName=" + getUserName() +
                ", category=" + categoryId +
                ", created=" + created +
                '}';
    }
}
