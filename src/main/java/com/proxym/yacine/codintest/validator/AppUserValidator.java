package com.proxym.yacine.codintest.validator;

import com.proxym.yacine.codintest.dto.request.NewAppUser;

import java.util.ArrayList;
import java.util.List;

public class AppUserValidator {

    public static List<String> validate(NewAppUser newAppUser) {
        ArrayList<String> errors = new ArrayList<>();
        if(newAppUser == null) {
            errors.add("User should not be null");
            return errors;
        }

        if(newAppUser.getFirstName().length()  == 0 || newAppUser.getLastName().length() == 0) {
            errors.add("First name is required ");
        } else if (newAppUser.getFirstName().length() == 1) {
            errors.add("First name should be more the one character");
        } else if(!newAppUser.getFirstName().matches("[a-zA-z, ' ']{2,30}")) {
            errors.add("First name should contain only alphabets");
        }

        if(newAppUser.getLastName().length()  == 0 || newAppUser.getLastName().length() == 0) {
            errors.add("Last name is required ");
        } else if (newAppUser.getLastName().length() == 1) {
            errors.add("Last name should be more than one character");
        } else if(!newAppUser.getLastName().matches("[a-zA-z, ' ']{2,30}")) {
            errors.add("Last name should contain only alphabets");
        }

        if(newAppUser.getEmail().length()  == 0 || newAppUser.getEmail().length() == 0) {
            errors.add("Email is required ");
        } else if(!newAppUser.getEmail().matches("[a-zA-Z][a-zA-Z0-9._%+-]{2,30}+@[a-z]+\\.[a-z]{2,4}")) {
            errors.add("Email is not valid");
        }

        if (newAppUser.getPassword().length() < 6) {
            errors.add("password should contain more than 6 characters");
        }

        return errors;
    }
}
