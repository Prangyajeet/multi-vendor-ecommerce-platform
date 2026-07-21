package com.prangyajeet.mvep.notification.service;

import com.prangyajeet.mvep.common.enums.NotificationType;
import com.prangyajeet.mvep.common.enums.RoleName;
import com.prangyajeet.mvep.exception.NotificationAccessDeniedException;
import com.prangyajeet.mvep.exception.NotificationNotFoundException;
import com.prangyajeet.mvep.notification.dto.NotificationRequestDTO;
import com.prangyajeet.mvep.notification.dto.NotificationResponseDTO;
import com.prangyajeet.mvep.notification.entity.Notification;
import com.prangyajeet.mvep.notification.repository.NotificationRepository;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserService userService) {

        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    /**
     * Returns the currently logged-in user.
     */
    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userService.getUserByEmail(email);
    }

    /**
     * Validates whether the logged-in user
     * can access the notification.
     *
     * Admin -> Can access all notifications.
     * Owner -> Can access own notification.
     * Others -> Access Denied.
     */
    private void validateNotificationAccess(
            Notification notification) {

        User currentUser = getCurrentUser();

        if (currentUser.getRole().getName() != RoleName.ADMIN
                && !notification.getUser().getId().equals(currentUser.getId())) {

            throw new NotificationAccessDeniedException(
                    "You are not allowed to access this notification."
            );
        }
    }

    /**
     * Create Notification
     */
    public NotificationResponseDTO createNotification(
            NotificationRequestDTO requestDTO) {

        User user = userService.getUserById(
                requestDTO.getUserId());

        Notification notification = new Notification();

        notification.setUser(user);

        notification.setTitle(
                requestDTO.getTitle());

        notification.setMessage(
                requestDTO.getMessage());

        notification.setNotificationType(
                requestDTO.getNotificationType());

        notification.setRead(false);

        Notification savedNotification =
                notificationRepository.save(notification);

        return convertToDTO(savedNotification);
    }

    /**
     * Get Notification By Id
     */
    public NotificationResponseDTO getNotificationById(
            Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new NotificationNotFoundException(
                                        "Notification not found with id : " + id
                                ));

        validateNotificationAccess(notification);

        return convertToDTO(notification);
    }
    /**
     * Get All Notifications
     * (Admin Only)
     */
    public List<NotificationResponseDTO> getAllNotifications() {

        return notificationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Get Logged-in User Notifications
     */
    public List<NotificationResponseDTO> getMyNotifications() {

        User currentUser = getCurrentUser();

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(
                        currentUser.getId())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Mark Notification As Read
     */
    public NotificationResponseDTO markAsRead(
            Long notificationId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new NotificationNotFoundException(
                                        "Notification not found with id : "
                                                + notificationId
                                ));

        validateNotificationAccess(notification);

        notification.setRead(true);

        Notification updatedNotification =
                notificationRepository.save(notification);

        return convertToDTO(updatedNotification);
    }
    /**
     * Send Notification To Single User
     */
    public void sendNotification(
            User user,
            String title,
            String message,
            NotificationType notificationType) {

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setNotificationType(notificationType);
        notification.setRead(false);

        notificationRepository.save(notification);
    }

    /**
     * Send Notification To All Admins
     */
    public void sendNotificationToAdmins(
            String title,
            String message,
            NotificationType notificationType) {

        List<User> admins = userService.getAllAdmins();

        for (User admin : admins) {

            sendNotification(
                    admin,
                    title,
                    message,
                    notificationType
            );
        }
    }

    /**
     * Send Welcome Notification
     */
    public void sendWelcomeNotification(
            User user) {

        sendNotification(
                user,
                "Welcome to MVEP 🎉",
                "Welcome to Multi Vendor E-Commerce Platform. Your account has been created successfully.",
                NotificationType.SYSTEM
        );
    }

    /**
     * Send Admin Notification
     */
    public void sendAdminNotification(
            String title,
            String message) {

        sendNotificationToAdmins(
                title,
                message,
                NotificationType.SYSTEM
        );
    }

    /**
     * Send Vendor Notification
     */
    public void sendVendorNotification(
            User vendor,
            String title,
            String message) {

        sendNotification(
                vendor,
                title,
                message,
                NotificationType.SYSTEM
        );
    }

    /**
     * Delete Notification
     */
    public void deleteNotification(
            Long notificationId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new NotificationNotFoundException(
                                        "Notification not found with id : "
                                                + notificationId
                                ));

        validateNotificationAccess(notification);

        notificationRepository.delete(notification);
    }

    /**
     * Convert Entity To DTO
     */
    private NotificationResponseDTO convertToDTO(
            Notification notification) {

        NotificationResponseDTO dto =
                new NotificationResponseDTO();

        dto.setId(
                notification.getId()
        );

        dto.setUserId(
                notification.getUser().getId()
        );

        dto.setTitle(
                notification.getTitle()
        );

        dto.setMessage(
                notification.getMessage()
        );

        dto.setNotificationType(
                notification.getNotificationType()
        );

        dto.setRead(
                notification.isRead()
        );

        dto.setCreatedAt(
                notification.getCreatedAt()
        );

        return dto;
    }
}