
package com.visitor.management.notification.repository;

import com.visitor.management.notification.entity.NotificationRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRecipientRepository
        extends JpaRepository<NotificationRecipient, UUID> {
}
