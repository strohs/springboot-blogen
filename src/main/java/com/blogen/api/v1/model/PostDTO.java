package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "title of the post", example = "Some amazing title", required = true)
    private String title;

    @Schema(description = "text of the post", example = "this is a post about cars", required = true)
    private String text;

    @Schema(description = "URL to an image on the web", example = "http://lorempixe/200/400/abstract")
    private String imageUrl;

    @Schema(description = "category this post belongs to", example = "Web Development", required = true)
    private String categoryName;

    @Schema(description = "userName of the user that created the post", example = "jimSmith01", required = true)
    private String userName;

    @Schema(description = "post creation time in ISO-8601", example = "2019-09-26T07:58:30", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created;

    @Schema(description = "URL that identifies this post", example = "/api/v1/posts/43", accessMode = Schema.AccessMode.READ_ONLY)
    private String postUrl;

    @Schema(description = "URL that identifies the parent of this post, will be null if the post is a parent", example = "/api/v1/posts/40", accessMode = Schema.AccessMode.READ_ONLY)
    private String parentPostUrl;

    @Schema(description = "list of child posts", accessMode = Schema.AccessMode.READ_ONLY)
    private List<PostDTO> children;
    

}
