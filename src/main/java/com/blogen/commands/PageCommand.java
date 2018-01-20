package com.blogen.commands;

import com.blogen.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A command object containing data used to display {@link PostCommand} data on a page. This command also
 * contains information on the current page and total number of pages that can be displayed.
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCommand {

    private int requestedPage;

    private int totalPages;

    //the total number of elements that could be retrieved from the Database
    private long totalElements;

    //id from the category table, of the category the user has selected
    private long selectedCategoryId;

    private String selectedCategoryName;

    //post to be displayed on the page
    List<PostCommand> posts;

    //contains all categories
    List<CategoryCommand> categories;
}
