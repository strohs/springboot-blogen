package com.blogen.services;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.CategoryPageCommand;
import com.blogen.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Data access methods for working with Blogen {@link Category}. Every post on blogen has an associated category.
 *
 * Categories can only be created by an admin of the Blogen site.
 *
 * Blogen categories should not be deleted once created, so delete operation are not supported
 *
 * @author Cliff
 */
public interface CategoryService {

    /**
     * get all the Blogen Categories in the database.
     * @return a list of {@link Category}, containing all the categories in the database
     */
    List<CategoryCommand> getAllCategories();

    /**
     * Retrieve a page of category data
     * @param pageNum the page of category data to retrieve
     * @return
     */
    CategoryPageCommand getAllCategories( int pageNum );

    /**
     * get a specific {@link Category} object
     * @param name - the name of the Category.name to search for
     * @return the {@link Category} containing the specified name
     */
    CategoryCommand getCategoryByName( String name );

    /**
     * get Categories with a name that is similar to the passed in name. This method will use a simple SQL
     * "LIKE" query to find matching names.
     * @param name the {@link Category} name to search for, should not contain wildcard characters (e.g. %)
     * @return a list of Categories that match the passed in name
     */
    List<CategoryCommand> getCategoryByNameLike( String name );

    /**
     * adds a new category to the category table
     * @param command command object of Category data to create in the DB
     * @return a command object representing the Category that was just created
     */
    CategoryCommand addCategoryByCategoryCommand( CategoryCommand command );


}
