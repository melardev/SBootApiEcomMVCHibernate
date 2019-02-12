package com.melardev.spring.shoppingcartweb.dtos.response.base;

import java.util.ArrayList;
import java.util.List;

public abstract class AppResponse {
    private Boolean success;
    private List<String> fullMessages;

    private MetaDiagnostics pageMeta;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<String> getFullMessages() {
        return fullMessages;
    }

    public void setFullMessages(List<String> fullMessages) {
        this.fullMessages = fullMessages;
    }

    public AppResponse() {

    }

    protected AppResponse(boolean success) {
        this.success = success;
        fullMessages = new ArrayList<>();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    protected void addFullMessage(String message) {
        if (fullMessages == null)
            fullMessages = new ArrayList<>();

        fullMessages.add(message);
    }

    public static class MetaDiagnostics {
        Long milliseconds;

        public Long getMilliseconds() {
            return milliseconds;
        }

        public void setMilliseconds(Long milliseconds) {
            this.milliseconds = milliseconds;
        }
    }


}
