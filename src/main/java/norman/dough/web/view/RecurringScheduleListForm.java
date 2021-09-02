package norman.dough.web.view;

import norman.dough.domain.RecurringSchedule;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class RecurringScheduleListForm extends ListForm<RecurringSchedule> {
    private List<RecurringScheduleListRow> rows = new ArrayList<>();

    public RecurringScheduleListForm(Page<RecurringSchedule> innerPage) {
        super(innerPage);
        for (RecurringSchedule entity : innerPage.getContent()) {
            RecurringScheduleListRow row = new RecurringScheduleListRow(entity);
            rows.add(row);
        }
    }

    public List<RecurringScheduleListRow> getRows() {
        return rows;
    }
}
