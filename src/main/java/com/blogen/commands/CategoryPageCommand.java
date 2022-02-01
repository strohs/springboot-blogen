package com.blogen.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Command object representing a page of {@link com.blogen.domain.Category} data
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPageCommand {

    private int requestedPage;

    private int totalPages;

    //the total number of elements that could be retrieved from the Database
    private long totalElements;

    //holds the new category name
    private String categoryName;

    //contains all categories
    List<CategoryCommand> categories;
}
