package norman.dough.web.view;

import norman.dough.domain.Account;
import norman.dough.domain.AccountType;
import norman.dough.domain.Category;

import java.math.BigDecimal;
import java.util.Date;

public class AccountView {
    private Long id;
    private Integer version;
    private String name;
    private AccountType type;
    private Long defaultCategoryId;
    private String defaultCategory;
    private Boolean active;

    public AccountView(Account entity) {
        id = entity.getId();
        version = entity.getVersion();
        name = entity.getName();
        type = entity.getType();
        if (entity.getDefaultCategory() != null) {
            defaultCategoryId = entity.getDefaultCategory().getId();
            defaultCategory = entity.getDefaultCategory().toString();
        }
        active = entity.getActive();
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    public Long getDefaultCategoryId() {
        return defaultCategoryId;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public Boolean getActive() {
        return active;
    }
}
