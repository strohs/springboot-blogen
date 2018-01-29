package com.blogen.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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

    @NotNull
    private String title;

    @NotNull
    private String text;

    private String imageUrl;

    @NotNull
    private String categoryName;

    @NotNull
    private String userName;

    private LocalDateTime created;

    private String postUrl;

    private String parentPostUrl;

    private List<PostDTO> children;
    

}
