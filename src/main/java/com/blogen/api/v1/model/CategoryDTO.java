package com.blogen.api.v1.model;

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

    private Long id;

    private String name;
}
