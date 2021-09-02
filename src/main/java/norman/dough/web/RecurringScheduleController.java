package norman.dough.web;

import norman.dough.domain.RecurringSchedule;
import norman.dough.domain.Account;
import norman.dough.domain.Category;
import norman.dough.exception.NotFoundException;
import norman.dough.exception.OptimisticLockingException;
import norman.dough.exception.ReferentialIntegrityException;
import norman.dough.service.RecurringScheduleService;
import norman.dough.service.AccountService;
import norman.dough.service.CategoryService;
import norman.dough.web.view.RecurringScheduleEditForm;
import norman.dough.web.view.RecurringScheduleListForm;
import norman.dough.web.view.RecurringScheduleView;
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
public class RecurringScheduleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecurringScheduleController.class);
    private static final String defaultSortColumn = "id";
    private static final String[] sortableColumns = {"cronString", "estimatedAmount", "comment", "tentativeAccount", "tentativeCategory"};
    @Autowired
    private RecurringScheduleService service;
    @Autowired
    private AccountService tentativeAccountService;
    @Autowired
    private CategoryService tentativeCategoryService;

    @GetMapping("/recurringScheduleList")
    public String loadRecurringScheduleList(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortColumn", required = false, defaultValue = "cronString") String sortColumn,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") Sort.Direction sortDirection,
            Model model) {

        // Convert sort column from string to an array of strings.
        String[] sortColumns = {defaultSortColumn};
        if (Arrays.asList(sortableColumns).contains(sortColumn)) {
            sortColumns = new String[]{sortColumn, defaultSortColumn};
        }

        // Get a page of records.
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sortDirection, sortColumns);
        Page<RecurringSchedule> page = service.findAll(pageable);

        // Display the page of records.
        RecurringScheduleListForm listForm = new RecurringScheduleListForm(page);
        model.addAttribute("listForm", listForm);
        return "recurringScheduleList";
    }

    @GetMapping("/recurringSchedule")
    public String loadRecurringScheduleView(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            RecurringSchedule entity = service.findById(id);
            RecurringScheduleView view = new RecurringScheduleView(entity);
            model.addAttribute("view", view);
            return "recurringScheduleView";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Recurring Schedule was not found.");
            return "redirect:/";
        }
    }

    @GetMapping("/recurringScheduleEdit")
    public String loadRecurringScheduleEdit(@RequestParam(value = "id", required = false) Long id,
            Model model, RedirectAttributes redirectAttributes) {

        // If no id, add new record.
        if (id == null) {
            model.addAttribute("editForm", new RecurringScheduleEditForm());
            return "recurringScheduleEdit";
        } else {

            // Otherwise, edit existing record.
            try {
                RecurringSchedule entity = service.findById(id);
                RecurringScheduleEditForm editForm = new RecurringScheduleEditForm(entity);
                model.addAttribute("editForm", editForm);
                return "recurringScheduleEdit";
            } catch (NotFoundException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Recurring Schedule was not found.");
                return "redirect:/";
            }
        }
    }

    @PostMapping("/recurringScheduleEdit")
    public String processRecurringScheduleEdit(@Valid @ModelAttribute("editForm") RecurringScheduleEditForm editForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "recurringScheduleEdit";
        }

        // Convert form to entity.
        Long id = editForm.getId();
        try {
            editForm.setTentativeAccountService(tentativeAccountService);
            editForm.setTentativeCategoryService(tentativeCategoryService);
            RecurringSchedule entity = editForm.toEntity();

            // Save entity.
            RecurringSchedule save = service.save(entity);
            String successMessage = "Recurring Schedule successfully added.";
            if (id != null) {
                successMessage = "Recurring Schedule successfully updated.";
            }
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            redirectAttributes.addAttribute("id", save.getId());
            return "redirect:/recurringSchedule?id={id}";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Recurring Schedule was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Recurring Schedule was updated by another user.");
            return "redirect:/";
        }
    }

    @PostMapping("/recurringScheduleDelete")
    public String processRecurringScheduleDelete(@RequestParam("id") Long id, @RequestParam("version") Integer version,
            RedirectAttributes redirectAttributes) {
        try {
            RecurringSchedule entity = service.findById(id);
            if (entity.getVersion() == version) {
                service.delete(entity);
                String successMessage = "Recurring Schedule successfully deleted.";
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
                return "redirect:/recurringScheduleList";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Recurring Schedule was updated by another user.");
                return "redirect:/";
            }
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Recurring Schedule was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Recurring Schedule was updated by another user.");
            return "redirect:/";
        } catch (ReferentialIntegrityException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Recurring Schedule cannot be deleted because other data depends on it.");
            return "redirect:/";
        }
    }

    @ModelAttribute("allTentativeAccount")
    public List<EntitySelectOption> loadTentativeAccountSelectOptions() {
        List<EntitySelectOption> options = new ArrayList<>();
        for (Account entity : tentativeAccountService.findAll()) {
            EntitySelectOption option = new EntitySelectOption(entity.getId(), entity.toString());
            options.add(option);
        }
        return options;
    }

    @ModelAttribute("allTentativeCategory")
    public List<EntitySelectOption> loadTentativeCategorySelectOptions() {
        List<EntitySelectOption> options = new ArrayList<>();
        for (Category entity : tentativeCategoryService.findAll()) {
            EntitySelectOption option = new EntitySelectOption(entity.getId(), entity.toString());
            options.add(option);
        }
        return options;
    }
}
