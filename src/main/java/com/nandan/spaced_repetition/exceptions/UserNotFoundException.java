package com.nandan.spaced_repetition.exceptions;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User '" + username + "' not found on LeetCode");
    }
}
