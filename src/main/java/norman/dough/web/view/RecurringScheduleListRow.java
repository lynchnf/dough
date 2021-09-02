package norman.dough.web.view;

import norman.dough.domain.RecurringSchedule;

import java.math.BigDecimal;

public class RecurringScheduleListRow {
    private Long id;
    private Integer version;
    private String cronString;
    private BigDecimal estimatedAmount;
    private String comment;
    private String tentativeAccount;
    private String tentativeCategory;

    public RecurringScheduleListRow(RecurringSchedule entity) {
        id = entity.getId();
        version = entity.getVersion();
        cronString = entity.getCronString();
        estimatedAmount = entity.getEstimatedAmount();
        comment = entity.getComment();
        tentativeAccount = entity.getTentativeAccount() == null ? null : entity.getTentativeAccount().toString();
        tentativeCategory = entity.getTentativeCategory() == null ? null : entity.getTentativeCategory().toString();
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getCronString() {
        return cronString;
    }

    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public String getComment() {
        return comment;
    }

    public String getTentativeAccount() {
        return tentativeAccount;
    }

    public String getTentativeCategory() {
        return tentativeCategory;
    }
}
