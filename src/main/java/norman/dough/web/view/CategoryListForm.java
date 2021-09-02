package norman.dough.web.view;

import norman.dough.domain.Category;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class CategoryListForm extends ListForm<Category> {
    private List<CategoryListRow> rows = new ArrayList<>();

    public CategoryListForm(Page<Category> innerPage) {
        super(innerPage);
        for (Category entity : innerPage.getContent()) {
            CategoryListRow row = new CategoryListRow(entity);
            rows.add(row);
        }
    }

    public List<CategoryListRow> getRows() {
        return rows;
    }
}
