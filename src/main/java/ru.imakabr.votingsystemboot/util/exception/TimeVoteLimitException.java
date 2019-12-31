package ru.imakabr.votingsystemboot.util.exception;

public class TimeVoteLimitException extends RuntimeException
{
    public TimeVoteLimitException(String message) {
        super(message);
    }
}
