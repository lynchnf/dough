package norman.dough.web.view;

import norman.dough.domain.Transaction;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class TransactionListForm extends ListForm<Transaction> {
    private List<TransactionListRow> rows = new ArrayList<>();
    private Long parentId;

    public TransactionListForm(Page<Transaction> innerPage) {
        super(innerPage);
        for (Transaction entity : innerPage.getContent()) {
            TransactionListRow row = new TransactionListRow(entity);
            rows.add(row);
        }
    }

    public List<TransactionListRow> getRows() {
        return rows;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
