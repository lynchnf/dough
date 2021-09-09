package norman.dough.domain;

import norman.dough.util.MiscUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurring_schedule_id")
    private RecurringSchedule recurringSchedule;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date estimatedDate;
    @Column(precision = 9, scale = 2, nullable = false)
    private BigDecimal estimatedAmount;
    @Column(length = 255)
    private String comment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tentative_account_id")
    private Account tentativeAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tentative_category_id")
    private Category tentativeCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actual_transaction_id")
    private Transaction actualTransaction;
    @Column(nullable = false)
    private Boolean skipped;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public RecurringSchedule getRecurringSchedule() {
        return recurringSchedule;
    }

    public void setRecurringSchedule(RecurringSchedule recurringSchedule) {
        this.recurringSchedule = recurringSchedule;
    }

    public Date getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(Date estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Account getTentativeAccount() {
        return tentativeAccount;
    }

    public void setTentativeAccount(Account tentativeAccount) {
        this.tentativeAccount = tentativeAccount;
    }

    public Category getTentativeCategory() {
        return tentativeCategory;
    }

    public void setTentativeCategory(Category tentativeCategory) {
        this.tentativeCategory = tentativeCategory;
    }

    public Transaction getActualTransaction() {
        return actualTransaction;
    }

    public void setActualTransaction(Transaction actualTransaction) {
        this.actualTransaction = actualTransaction;
    }

    public Boolean getSkipped() {
        return skipped;
    }

    public void setSkipped(Boolean skipped) {
        this.skipped = skipped;
    }

    @Override
    public String toString() {
        return comment == null ? MiscUtils.YYMD.format(estimatedDate) : comment;
    }
}
