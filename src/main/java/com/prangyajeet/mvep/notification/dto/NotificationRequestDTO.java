package com.prangyajeet.mvep.notification.dto;

import com.prangyajeet.mvep.common.enums.NotificationType;

public class NotificationRequestDTO {

    private Long userId;

    private String title;

    private String message;

    private NotificationType notificationType;

    public NotificationRequestDTO() {
    }

    public NotificationRequestDTO(Long userId,
                                  String title,
                                  String message,
                                  NotificationType notificationType) {

        this.userId = userId;
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}