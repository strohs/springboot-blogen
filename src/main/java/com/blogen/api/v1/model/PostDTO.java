package com.blogen.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

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

    private String title;

    private String text;

    private String imageUrl;

    private String categoryName;

    private String userName;

    private LocalDateTime created;

    private String postUrl;

    private String parentPostUrl;

    private List<PostDTO> children;
    

}
