package norman.dough.web.view;

import norman.dough.domain.Account;
import norman.dough.domain.AccountNumber;
import norman.dough.domain.AccountType;
import norman.dough.domain.Category;
import norman.dough.exception.NotFoundException;
import norman.dough.service.CategoryService;
import norman.dough.web.view.validation.AfterDateIfValueChange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@AfterDateIfValueChange(newDate = "effectiveDate", oldDate = "oldEffectiveDate", newString = "number", oldString = "oldNumber")
public class AccountEditForm {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountEditForm.class);
    private CategoryService defaultCategoryService;
    private Long id;
    private Integer version = 0;
    @NotBlank(message = "Name may not be blank.")
    @Size(max = 50, message = "Name may not be over {max} characters long.")
    private String name;
    @NotNull(message = "Type may not be blank.")
    private AccountType type;
    private Long defaultCategoryId;
    private String defaultCategory;
    @NotBlank(message = "Number may not be blank.")
    @Size(max = 50, message = "Number may not be over {max} characters long.")
    private String number;
    private String oldNumber;
    @NotNull(message = "Effective Date may not be blank.")
    @DateTimeFormat(pattern = "M/d/yyyy")
    private Date effectiveDate;
    @DateTimeFormat(pattern = "M/d/yyyy")
    private Date oldEffectiveDate;
    //    @NotNull(message = "Opening Amount may not be blank.")
    //    @Digits(integer = 7, fraction = 2, message = "Opening Amount value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    //    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    //    private BigDecimal amount;
    @NotNull(message = "Active may not be blank.")
    private Boolean active;

    public AccountEditForm() {
        active = true;
    }

    public AccountEditForm(Account entity, AccountNumber accountNumber) {
        id = entity.getId();
        version = entity.getVersion();
        name = entity.getName();
        type = entity.getType();
        if (entity.getDefaultCategory() != null) {
            defaultCategoryId = entity.getDefaultCategory().getId();
            defaultCategory = entity.getDefaultCategory().toString();
        }
        number = accountNumber.getNumber();
        oldNumber = accountNumber.getNumber();
        effectiveDate = accountNumber.getEffectiveDate();
        oldEffectiveDate = accountNumber.getEffectiveDate();
        active = entity.getActive();
    }

    public Account toEntity() throws NotFoundException {
        Account entity = new Account();
        entity.setId(id);
        entity.setVersion(version);
        entity.setName(StringUtils.trimToNull(name));
        entity.setType(type);
        if (defaultCategoryId != null) {
            Category defaultCategory = defaultCategoryService.findById(defaultCategoryId);
            entity.setDefaultCategory(defaultCategory);
        }
        entity.setActive(active);
        return entity;
    }

    public AccountNumber toAccountNumber() {
        AccountNumber accountNumber = new AccountNumber();
        accountNumber.setNumber(StringUtils.trimToNull(number));
        accountNumber.setEffectiveDate(effectiveDate);
        return accountNumber;
    }

    public void setDefaultCategoryService(CategoryService defaultCategoryService) {
        this.defaultCategoryService = defaultCategoryService;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Long getDefaultCategoryId() {
        return defaultCategoryId;
    }

    public void setDefaultCategoryId(Long defaultCategoryId) {
        this.defaultCategoryId = defaultCategoryId;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(String defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOldNumber() {
        return oldNumber;
    }

    public void setOldNumber(String oldNumber) {
        this.oldNumber = oldNumber;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getOldEffectiveDate() {
        return oldEffectiveDate;
    }

    public void setOldEffectiveDate(Date oldEffectiveDate) {
        this.oldEffectiveDate = oldEffectiveDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
