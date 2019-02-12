package com.melardev.spring.shoppingcartweb.models;

import com.melardev.spring.shoppingcartweb.enums.NotificationType;
import com.melardev.spring.shoppingcartweb.services.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Notification {
    String title;
    //    String message;
    List<String> messages;
    List<String> htmlClasses = new ArrayList<>();

    public Notification(String message, NotificationType notificationType) {
        this(StringHelper.toTitleCase(notificationType.toString()), message, notificationType);
    }

    public Notification(String title, String message, NotificationType notificationType) {
        this(title, Arrays.asList(message), notificationType);
    }

    public Notification(String title, List<String> messages, NotificationType notificationType) {

        this.messages = messages;

        switch (notificationType) {
            case SUCCESS:
                htmlClasses.add("alert-success");
                break;
            case INFO:
                htmlClasses.add("alert-info");
                break;
            case ERROR:
                htmlClasses.add("alert-danger");
                break;
            case WARNING:
                htmlClasses.add("alert-warning");
                break;
            case NOTICE:
                htmlClasses.add("alert-notice");
            default:
                break;
        }
    }

    String getSpaceSepratedClasses() {
        return StringHelper.collectionToSpaceDelimitedString(htmlClasses);
    }
}
