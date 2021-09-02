package norman.dough.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.math.BigDecimal;

@Entity
public class RecurringSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version = 0;
    @Column(length = 20, nullable = false)
    private String cronString;
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

    public String getCronString() {
        return cronString;
    }

    public void setCronString(String cronString) {
        this.cronString = cronString;
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

    @Override
    public String toString() {
        return comment == null ? cronString : comment;
    }
}
