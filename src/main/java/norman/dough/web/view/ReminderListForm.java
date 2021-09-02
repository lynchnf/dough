package norman.dough.web.view;

import norman.dough.domain.Reminder;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class ReminderListForm extends ListForm<Reminder> {
    private List<ReminderListRow> rows = new ArrayList<>();

    public ReminderListForm(Page<Reminder> innerPage) {
        super(innerPage);
        for (Reminder entity : innerPage.getContent()) {
            ReminderListRow row = new ReminderListRow(entity);
            rows.add(row);
        }
    }

    public List<ReminderListRow> getRows() {
        return rows;
    }
}
