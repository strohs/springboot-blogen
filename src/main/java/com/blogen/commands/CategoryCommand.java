package com.blogen.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Command object for transferring {@link com.blogen.domain.Category} data between the server and web-pages
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
public class CategoryCommand {

    private Long id;

    private String name;

    private String created;

}
