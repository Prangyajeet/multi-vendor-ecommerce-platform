package com.prangyajeet.mvep.notification.contorller;

import com.prangyajeet.mvep.notification.dto.NotificationRequestDTO;
import com.prangyajeet.mvep.notification.dto.NotificationResponseDTO;
import com.prangyajeet.mvep.notification.service.NotificationService;
import com.prangyajeet.mvep.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Create Notification
     */
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> createNotification(
            @RequestBody NotificationRequestDTO requestDTO) {

        NotificationResponseDTO response =
                notificationService.createNotification(requestDTO);

        return new ResponseEntity<>(
                ApiResponse.success(
                        "Notification created successfully.",
                        response
                ),
                HttpStatus.CREATED
        );
    }

    /**
     * Get Logged-in User Notifications
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<NotificationResponseDTO>>> getMyNotifications() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notifications fetched successfully.",
                        notificationService.getMyNotifications()
                )
        );
    }

    /**
     * Get Notification By Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> getNotificationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notification fetched successfully.",
                        notificationService.getNotificationById(id)
                )
        );
    }

    /**
     * Mark Notification As Read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> markAsRead(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notification marked as read.",
                        notificationService.markAsRead(id)
                )
        );
    }

    /**
     * Delete Notification
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteNotification(
            @PathVariable Long id) {

        notificationService.deleteNotification(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notification deleted successfully.",
                        "Deleted"
                )
        );
    }
}