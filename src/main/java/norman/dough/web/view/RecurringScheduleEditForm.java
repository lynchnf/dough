package norman.dough.web.view;

import norman.dough.domain.RecurringSchedule;
import norman.dough.domain.Account;
import norman.dough.domain.Category;
import norman.dough.exception.NotFoundException;
import norman.dough.service.AccountService;
import norman.dough.service.CategoryService;
import norman.dough.util.MiscUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

public class RecurringScheduleEditForm {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecurringScheduleEditForm.class);
    private AccountService tentativeAccountService;
    private CategoryService tentativeCategoryService;
    private Long id;
    private Integer version = 0;
    @NotBlank(message = "Cron String may not be blank.")
    @Size(max = 20, message = "Cron String may not be over {max} characters long.")
    private String cronString;
    @NotNull(message = "Estimated Amount may not be blank.")
    @Digits(integer = 7, fraction = 2, message = "Estimated Amount value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal estimatedAmount;
    @Size(max = 255, message = "Comment may not be over {max} characters long.")
    private String comment;
    private Long tentativeAccountId;
    private String tentativeAccount;
    private Long tentativeCategoryId;
    private String tentativeCategory;

    public RecurringScheduleEditForm() {
    }

    public RecurringScheduleEditForm(RecurringSchedule entity) {
        id = entity.getId();
        version = entity.getVersion();
        cronString = entity.getCronString();
        estimatedAmount = entity.getEstimatedAmount();
        comment = entity.getComment();
        if (entity.getTentativeAccount() != null) {
            tentativeAccountId = entity.getTentativeAccount().getId();
            tentativeAccount = entity.getTentativeAccount().toString();
        }
        if (entity.getTentativeCategory() != null) {
            tentativeCategoryId = entity.getTentativeCategory().getId();
            tentativeCategory = entity.getTentativeCategory().toString();
        }
    }

    public RecurringSchedule toEntity() throws NotFoundException {
        RecurringSchedule entity = new RecurringSchedule();
        entity.setId(id);
        entity.setVersion(version);
        entity.setCronString(StringUtils.trimToNull(cronString));
        entity.setEstimatedAmount(estimatedAmount);
        entity.setComment(StringUtils.trimToNull(comment));
        if (tentativeAccountId != null) {
            Account tentativeAccount = tentativeAccountService.findById(tentativeAccountId);
            entity.setTentativeAccount(tentativeAccount);
        }
        if (tentativeCategoryId != null) {
            Category tentativeCategory = tentativeCategoryService.findById(tentativeCategoryId);
            entity.setTentativeCategory(tentativeCategory);
        }
        return entity;
    }

    public void setTentativeAccountService(AccountService tentativeAccountService) {
        this.tentativeAccountService = tentativeAccountService;
    }

    public void setTentativeCategoryService(CategoryService tentativeCategoryService) {
        this.tentativeCategoryService = tentativeCategoryService;
    }

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

    public Long getTentativeAccountId() {
        return tentativeAccountId;
    }

    public void setTentativeAccountId(Long tentativeAccountId) {
        this.tentativeAccountId = tentativeAccountId;
    }

    public String getTentativeAccount() {
        return tentativeAccount;
    }

    public Long getTentativeCategoryId() {
        return tentativeCategoryId;
    }

    public void setTentativeCategoryId(Long tentativeCategoryId) {
        this.tentativeCategoryId = tentativeCategoryId;
    }

    public String getTentativeCategory() {
        return tentativeCategory;
    }
}
