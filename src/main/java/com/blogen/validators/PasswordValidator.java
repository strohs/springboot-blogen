package com.blogen.validators;

import com.blogen.commands.UserProfileCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for ensuring password field and password confirmation fields match.
 * Password and password confirmation are only validated if something was entered into one of the fields. Blank fields
 * are left alone and not validated.
 *
 * @author Cliff
 */
@Component
public class PasswordValidator implements Validator {
    @Override
    public boolean supports( Class<?> clazz ) {
        return UserProfileCommand.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {

        //ValidationUtils.rejectIfEmptyOrWhitespace( errors, "password","required.password","Field name is required" );

        //ValidationUtils.rejectIfEmptyOrWhitespace( errors, "confirmPassword","required.confirmPassword" ,"Field name is required" );

        UserProfileCommand command = (UserProfileCommand) target;

        //if something was entered in the password field
        if( command.getPassword() != null && command.getPassword().length() > 0 ) {
            if ( !passwordLengthOk( command.getPassword() ) ) {
                errors.rejectValue( "password","size.password", "size must be between 8 and 30 characters" );
            }
        }
        //if something was entered in the password confirmation field
        if( command.getConfirmPassword() != null && command.getConfirmPassword().length() > 0 ) {
            if ( !passwordLengthOk( command.getConfirmPassword() ) ) {
                errors.rejectValue( "confirmPassword","size.password", "size must be between 8 and 30 characters" );
            }
        }

        if ( command.getPassword() != null ) {
            if( !(command.getPassword().equals( command.getConfirmPassword() )) ) {
                errors.rejectValue( "password","nomatch.password" );
            }
        }


    }

    //password must be between 8 and 30 characters
    private boolean passwordLengthOk( String password ) {
        return password.length() >= 8 && password.length() <= 30;
    }
}
