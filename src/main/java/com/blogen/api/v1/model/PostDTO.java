package com.blogen.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Blogen {@link com.blogen.domain.Post} data
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long id;

    private Long parentId;

    private Long userId;

    private String userName;

    private String title;

    private String text;

    private String imageUrl;

    private String categoryName;

    private LocalDateTime created;

    private List<PostDTO> children;

    /**
     * A parent post will have a parentId == null.
     * @return true if this is a 'parent' post, false otherwise (indicating that this post is a 'child' post)
     */
    public boolean isParentPost() {
        return parentId == null;
    }

}
