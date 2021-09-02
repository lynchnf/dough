package norman.dough.web;

import norman.dough.domain.Category;
import norman.dough.exception.NotFoundException;
import norman.dough.exception.OptimisticLockingException;
import norman.dough.exception.ReferentialIntegrityException;
import norman.dough.service.CategoryService;
import norman.dough.web.view.CategoryEditForm;
import norman.dough.web.view.CategoryListForm;
import norman.dough.web.view.CategoryView;
import norman.dough.web.view.EntitySelectOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CategoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    private static final String defaultSortColumn = "id";
    private static final String[] sortableColumns = {"name", "parentCategory"};
    @Autowired
    private CategoryService service;
    @Autowired
    private CategoryService parentCategoryService;

    @GetMapping("/categoryList")
    public String loadCategoryList(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortColumn", required = false, defaultValue = "name") String sortColumn,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") Sort.Direction sortDirection,
            Model model) {

        // Convert sort column from string to an array of strings.
        String[] sortColumns = {defaultSortColumn};
        if (Arrays.asList(sortableColumns).contains(sortColumn)) {
            sortColumns = new String[]{sortColumn, defaultSortColumn};
        }

        // Get a page of records.
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sortDirection, sortColumns);
        Page<Category> page = service.findAll(pageable);

        // Display the page of records.
        CategoryListForm listForm = new CategoryListForm(page);
        model.addAttribute("listForm", listForm);
        return "categoryList";
    }

    @GetMapping("/category")
    public String loadCategoryView(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Category entity = service.findById(id);
            CategoryView view = new CategoryView(entity);
            model.addAttribute("view", view);
            return "categoryView";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category was not found.");
            return "redirect:/";
        }
    }

    @GetMapping("/categoryEdit")
    public String loadCategoryEdit(@RequestParam(value = "id", required = false) Long id, Model model,
            RedirectAttributes redirectAttributes) {

        // If no id, add new record.
        if (id == null) {
            model.addAttribute("editForm", new CategoryEditForm());
            return "categoryEdit";
        } else {

            // Otherwise, edit existing record.
            try {
                Category entity = service.findById(id);
                CategoryEditForm editForm = new CategoryEditForm(entity);
                model.addAttribute("editForm", editForm);
                return "categoryEdit";
            } catch (NotFoundException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Category was not found.");
                return "redirect:/";
            }
        }
    }

    @PostMapping("/categoryEdit")
    public String processCategoryEdit(@Valid @ModelAttribute("editForm") CategoryEditForm editForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "categoryEdit";
        }

        // Convert form to entity.
        Long id = editForm.getId();
        try {
            editForm.setParentCategoryService(parentCategoryService);
            Category entity = editForm.toEntity();

            // Save entity.
            Category save = service.save(entity);
            String successMessage = "Category successfully added.";
            if (id != null) {
                successMessage = "Category successfully updated.";
            }
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            redirectAttributes.addAttribute("id", save.getId());
            return "redirect:/category?id={id}";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category was updated by another user.");
            return "redirect:/";
        }
    }

    @PostMapping("/categoryDelete")
    public String processCategoryDelete(@RequestParam("id") Long id, @RequestParam("version") Integer version,
            RedirectAttributes redirectAttributes) {
        try {
            Category entity = service.findById(id);
            if (entity.getVersion() == version) {
                service.delete(entity);
                String successMessage = "Category successfully deleted.";
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
                return "redirect:/categoryList";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Category was updated by another user.");
                return "redirect:/";
            }
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category was updated by another user.");
            return "redirect:/";
        } catch (ReferentialIntegrityException e) {
            redirectAttributes
                    .addFlashAttribute("errorMessage", "Category cannot be deleted because other data depends on it.");
            return "redirect:/";
        }
    }

    @ModelAttribute("allParentCategory")
    public List<EntitySelectOption> loadParentCategorySelectOptions() {
        List<EntitySelectOption> options = new ArrayList<>();
        for (Category entity : parentCategoryService.findAll()) {
            EntitySelectOption option = new EntitySelectOption(entity.getId(), entity.toString());
            options.add(option);
        }
        return options;
    }
}
