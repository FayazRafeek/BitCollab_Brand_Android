package com.fyzanz.bitcollab.Model.Data;

public class BasicResponse {

    String status; //Loading, Success, Error

    Throwable error;

    public BasicResponse() {
    }

    public BasicResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
