package norman.dough.web.view;

import norman.dough.domain.Category;
import norman.dough.domain.Category;

import java.math.BigDecimal;
import java.util.Date;

public class CategoryView {
    private Long id;
    private Integer version;
    private String name;
    private String description;
    private Long parentCategoryId;
    private String parentCategory;

    public CategoryView(Category entity) {
        id = entity.getId();
        version = entity.getVersion();
        name = entity.getName();
        description = entity.getDescription();
        if (entity.getParentCategory() != null) {
            parentCategoryId = entity.getParentCategory().getId();
            parentCategory = entity.getParentCategory().toString();
        }
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

    public String getDescription() {
        return description;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public String getParentCategory() {
        return parentCategory;
    }
}
