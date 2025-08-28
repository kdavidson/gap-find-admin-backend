package gov.cabinetoffice.gap.adminbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cabinetoffice.gap.adminbackend.enums.SpotlightBatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "spotlight_batch")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpotlightBatch {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "status", nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private SpotlightBatchStatus status = SpotlightBatchStatus.QUEUED;

    @Column(name = "last_send_attempt")
    private Instant lastSendAttempt;

    @Column
    private int version;

    @Column(name = "created", nullable = false)
    @Builder.Default
    private Instant created = Instant.now();

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @ManyToMany
    @JoinTable(name = "spotlight_batch_submission", joinColumns = @JoinColumn(name = "spotlight_batch_id"),
            inverseJoinColumns = @JoinColumn(name = "spotlight_submission_id"))
    @JsonIgnoreProperties("batches")
    @Builder.Default
    private List<SpotlightSubmission> spotlightSubmissions = new ArrayList<>();

}
