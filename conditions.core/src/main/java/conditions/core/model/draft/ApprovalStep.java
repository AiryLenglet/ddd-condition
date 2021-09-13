package conditions.core.model.draft;

import conditions.core.event.approval.AskedChangeEvent;
import conditions.core.event.approval.ConditionAcceptedEvent;
import conditions.core.event.approval.ConditionRejectedEvent;
import conditions.core.model.Aggregate;
import conditions.core.model.ConditionId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalStep extends Aggregate {

    @EmbeddedId
    private ApprovalStepId approvalStepId = new ApprovalStepId();
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "CONDITION_ID"))
    })
    private ConditionId conditionId;

    private String justification;

    private Instant creationDate;

    public ApprovalStep(ConditionId conditionId, Instant creationDate) {
        this.conditionId = conditionId;
        this.creationDate = creationDate;
    }

    ApprovalStep() {
    }

    public void askForChange() {
        validateStep();
        this.addEvent(new AskedChangeEvent(this.conditionId, this.approvalStepId));
    }

    public void accept() {
        this.addEvent(new ConditionAcceptedEvent(this.conditionId, this.approvalStepId));
    }

    public void reject() {
        validateStep();
        this.addEvent(new ConditionRejectedEvent(this.conditionId, this.approvalStepId));
    }

    public ApprovalStepId getApprovalStepId() {
        return approvalStepId;
    }

    public ConditionId getConditionId() {
        return conditionId;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    private void validateStep() {
        if (this.justification == null || this.justification.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
