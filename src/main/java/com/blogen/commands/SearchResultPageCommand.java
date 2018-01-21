package com.blogen.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A command object containing data to be displayed on the search result page.
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultPageCommand {

    private int requestedPage;

    private int totalPages;

    //the total number of elements that could be retrieved from the Database
    private long totalElements;

    //the string that was searched for
    private String searchStr;

    //post to be displayed on the page
    List<PostCommand> posts;


}
