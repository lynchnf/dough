package norman.dough.web.view;

import norman.dough.domain.Account;
import norman.dough.domain.AccountType;

public class AccountListRow {
    private Long id;
    private Integer version;
    private String name;
    private AccountType type;
    private String defaultCategory;
    private Boolean active;

    public AccountListRow(Account entity) {
        id = entity.getId();
        version = entity.getVersion();
        name = entity.getName();
        type = entity.getType();
        defaultCategory = entity.getDefaultCategory() == null ? null : entity.getDefaultCategory().toString();
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

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public Boolean getActive() {
        return active;
    }
}
