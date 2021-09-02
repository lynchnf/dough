package norman.dough.web.view;

import norman.dough.domain.Statement;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class StatementListForm extends ListForm<Statement> {
    private List<StatementListRow> rows = new ArrayList<>();
    private Long parentId;

    public StatementListForm(Page<Statement> innerPage) {
        super(innerPage);
        for (Statement entity : innerPage.getContent()) {
            StatementListRow row = new StatementListRow(entity);
            rows.add(row);
        }
    }

    public List<StatementListRow> getRows() {
        return rows;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
