package com.tiy.practice;

/**
 * Created by crci1 on 2/9/2017.
 */
public class StatusMessage {
    private boolean success = true;
    // this will only have a value if there is an error
    // it will be null otherwise
    private String errorMessage;

    public StatusMessage() {
    }

    public StatusMessage(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
