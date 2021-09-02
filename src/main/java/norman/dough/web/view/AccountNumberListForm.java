package norman.dough.web.view;

import norman.dough.domain.AccountNumber;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class AccountNumberListForm extends ListForm<AccountNumber> {
    private List<AccountNumberListRow> rows = new ArrayList<>();
    private Long parentId;

    public AccountNumberListForm(Page<AccountNumber> innerPage) {
        super(innerPage);
        for (AccountNumber entity : innerPage.getContent()) {
            AccountNumberListRow row = new AccountNumberListRow(entity);
            rows.add(row);
        }
    }

    public List<AccountNumberListRow> getRows() {
        return rows;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
