package com.prangyajeet.mvep.notification.contorller;

import com.prangyajeet.mvep.notification.dto.NotificationResponseDTO;
import com.prangyajeet.mvep.notification.service.NotificationService;
import com.prangyajeet.mvep.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notifications")
@CrossOrigin(origins = "*")
public class AdminNotificationController {

    private final NotificationService notificationService;

    public AdminNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Get all notifications in the system.
     * Access: ADMIN only
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponseDTO>>> getAllNotifications() {

        List<NotificationResponseDTO> notifications =
                notificationService.getAllNotifications();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "All notifications fetched successfully.",
                        notifications
                )
        );
    }
}