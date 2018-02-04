package com.blogen.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorsView {

    private List<ApiFieldError> fieldError;
    private List<ApiGlobalError> globalError;

}
