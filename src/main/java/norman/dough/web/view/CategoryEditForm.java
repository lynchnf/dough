package norman.dough.web.view;

import norman.dough.domain.Category;
import norman.dough.exception.NotFoundException;
import norman.dough.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CategoryEditForm {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryEditForm.class);
    private CategoryService parentCategoryService;
    private Long id;
    private Integer version = 0;
    @NotBlank(message = "Name may not be blank.")
    @Size(max = 20, message = "Name may not be over {max} characters long.")
    private String name;
    @Size(max = 255, message = "Description may not be over {max} characters long.")
    private String description;
    private Long parentCategoryId;
    private String parentCategory;

    public CategoryEditForm() {
    }

    public CategoryEditForm(Category entity) {
        id = entity.getId();
        version = entity.getVersion();
        name = entity.getName();
        description = entity.getDescription();
        if (entity.getParentCategory() != null) {
            parentCategoryId = entity.getParentCategory().getId();
            parentCategory = entity.getParentCategory().toString();
        }
    }

    public Category toEntity() throws NotFoundException {
        Category entity = new Category();
        entity.setId(id);
        entity.setVersion(version);
        entity.setName(StringUtils.trimToNull(name));
        entity.setDescription(StringUtils.trimToNull(description));
        if (parentCategoryId != null) {
            Category parentCategory = parentCategoryService.findById(parentCategoryId);
            entity.setParentCategory(parentCategory);
        }
        return entity;
    }

    public void setParentCategoryService(CategoryService parentCategoryService) {
        this.parentCategoryService = parentCategoryService;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getParentCategory() {
        return parentCategory;
    }
}
