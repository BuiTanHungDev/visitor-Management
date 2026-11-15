
package com.visitor.management.notification.entity;

import com.visitor.management.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "notification_recipients",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_notification_recipient",
            columnNames = {
                "notification_id",
                "user_id",
                "delivery_channel"
            }
        )
    },
    indexes = {
        @Index(
            name = "idx_notification_recipients_user",
            columnList = "user_id"
        ),
        @Index(
            name = "idx_notification_recipients_status",
            columnList = "delivery_status"
        )
    }
)
public class NotificationRecipient {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(
        name = "delivery_channel",
        nullable = false,
        length = 20
    )
    private String deliveryChannel = "IN_APP";

    @Column(
        name = "delivery_status",
        nullable = false,
        length = 20
    )
    private String deliveryStatus = "PENDING";

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Column(name = "read_at")
    private Instant readAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(
        name = "created_at",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private Instant createdAt;
}
