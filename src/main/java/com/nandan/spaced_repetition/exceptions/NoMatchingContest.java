package com.nandan.spaced_repetition.exceptions;

public class NoMatchingContest extends RuntimeException {
    public NoMatchingContest(String message) {
        super(message);
    }
}
