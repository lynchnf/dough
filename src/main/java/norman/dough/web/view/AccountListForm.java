package norman.dough.web.view;

import norman.dough.domain.Account;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class AccountListForm extends ListForm<Account> {
    private List<AccountListRow> rows = new ArrayList<>();

    public AccountListForm(Page<Account> innerPage) {
        super(innerPage);
        for (Account entity : innerPage.getContent()) {
            AccountListRow row = new AccountListRow(entity);
            rows.add(row);
        }
    }

    public List<AccountListRow> getRows() {
        return rows;
    }
}
