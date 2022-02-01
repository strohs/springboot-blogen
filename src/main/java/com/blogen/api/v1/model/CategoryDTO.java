package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object containing {@link com.blogen.domain.Category} data to be exposed to clients.
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @Schema(description = "category name", example = "Business", required = true)
    private String name;

    @Schema(description = "URL of this Category", example = "/api/v1/categories/4", accessMode = Schema.AccessMode.READ_ONLY)
    private String categoryUrl;
}
