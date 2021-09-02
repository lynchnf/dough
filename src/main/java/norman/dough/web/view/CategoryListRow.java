package norman.dough.web.view;

import norman.dough.domain.Category;

public class CategoryListRow {
    private Long id;
    private Integer version;
    private String name;
    private String description;
    private String parentCategory;

    public CategoryListRow(Category entity) {
        id = entity.getId();
        version = entity.getVersion();
        name = entity.getName();
        description = entity.getDescription();
        parentCategory = entity.getParentCategory() == null ? null : entity.getParentCategory().toString();
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

    public String getParentCategory() {
        return parentCategory;
    }
}
