package ru.imakabr.votingsystemboot.util.exception;

public enum ErrorType {
    APP_ERROR("Application error!!"),
    DATA_NOT_FOUND("Data not found!!"),
    DATA_ERROR("Data error!!"),
    VALIDATION_ERROR("Validation error!!"),
    VOTING_ERROR("Voting error!!"),
    WRONG_REQUEST("Wrong request!!");

    private final String errorCode;

    ErrorType(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
