
package com.visitor.management.gate.entity;

import com.visitor.management.soldier.entity.Soldier;
import com.visitor.management.user.entity.User;
import com.visitor.management.visit.entity.VisitParticipantRecord;
import com.visitor.management.visit.entity.VisitSession;
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
@Table(name = "gate_verification_logs", indexes = {
        @Index(name = "idx_gate_logs_session", columnList = "session_id"),
        @Index(name = "idx_gate_logs_participant", columnList = "participant_record_id"),
        @Index(name = "idx_gate_logs_soldier", columnList = "soldier_id"),
        @Index(name = "idx_gate_logs_checked_at", columnList = "checked_at")
})
public class GateVerificationLog {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private VisitSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_record_id")
    private VisitParticipantRecord participantRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soldier_id")
    private Soldier soldier;

    @Column(name = "subject_type", nullable = false, length = 20)
    private String subjectType;

    @Column(name = "event_type", nullable = false, length = 30)
    private String eventType;

    @Column(name = "verification_result", nullable = false, length = 20)
    private String verificationResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_by_user_id", nullable = false)
    private User checkedByUser;

    @Column(name = "gate_code", nullable = false, length = 50)
    private String gateCode = "MAIN_GATE";

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "checked_at", nullable = false, insertable = false, updatable = false)
    private Instant checkedAt;
}
