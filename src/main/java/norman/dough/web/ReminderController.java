package norman.dough.web;

import norman.dough.domain.Reminder;
import norman.dough.domain.RecurringSchedule;
import norman.dough.domain.Account;
import norman.dough.domain.Category;
import norman.dough.domain.Transaction;
import norman.dough.exception.NotFoundException;
import norman.dough.exception.OptimisticLockingException;
import norman.dough.exception.ReferentialIntegrityException;
import norman.dough.service.ReminderService;
import norman.dough.service.RecurringScheduleService;
import norman.dough.service.AccountService;
import norman.dough.service.CategoryService;
import norman.dough.service.TransactionService;
import norman.dough.web.view.ReminderEditForm;
import norman.dough.web.view.ReminderListForm;
import norman.dough.web.view.ReminderView;
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
public class ReminderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderController.class);
    private static final String defaultSortColumn = "id";
    private static final String[] sortableColumns = {"estimatedDate", "estimatedAmount", "comment", "tentativeAccount", "tentativeCategory", "skipped"};
    @Autowired
    private ReminderService service;
    @Autowired
    private RecurringScheduleService recurringScheduleService;
    @Autowired
    private AccountService tentativeAccountService;
    @Autowired
    private CategoryService tentativeCategoryService;
    @Autowired
    private TransactionService actualTransactionService;

    @GetMapping("/reminderList")
    public String loadReminderList(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortColumn", required = false, defaultValue = "estimatedDate") String sortColumn,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") Sort.Direction sortDirection,
            Model model) {

        // Convert sort column from string to an array of strings.
        String[] sortColumns = {defaultSortColumn};
        if (Arrays.asList(sortableColumns).contains(sortColumn)) {
            sortColumns = new String[]{sortColumn, defaultSortColumn};
        }

        // Get a page of records.
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sortDirection, sortColumns);
        Page<Reminder> page = service.findAll(pageable);

        // Display the page of records.
        ReminderListForm listForm = new ReminderListForm(page);
        model.addAttribute("listForm", listForm);
        return "reminderList";
    }

    @GetMapping("/reminder")
    public String loadReminderView(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Reminder entity = service.findById(id);
            ReminderView view = new ReminderView(entity);
            model.addAttribute("view", view);
            return "reminderView";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reminder was not found.");
            return "redirect:/";
        }
    }

    @GetMapping("/reminderEdit")
    public String loadReminderEdit(@RequestParam(value = "id", required = false) Long id,
            Model model, RedirectAttributes redirectAttributes) {

        // If no id, add new record.
        if (id == null) {
            model.addAttribute("editForm", new ReminderEditForm());
            return "reminderEdit";
        } else {

            // Otherwise, edit existing record.
            try {
                Reminder entity = service.findById(id);
                ReminderEditForm editForm = new ReminderEditForm(entity);
                model.addAttribute("editForm", editForm);
                return "reminderEdit";
            } catch (NotFoundException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Reminder was not found.");
                return "redirect:/";
            }
        }
    }

    @PostMapping("/reminderEdit")
    public String processReminderEdit(@Valid @ModelAttribute("editForm") ReminderEditForm editForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reminderEdit";
        }

        // Convert form to entity.
        Long id = editForm.getId();
        try {
            editForm.setRecurringScheduleService(recurringScheduleService);
            editForm.setTentativeAccountService(tentativeAccountService);
            editForm.setTentativeCategoryService(tentativeCategoryService);
            editForm.setActualTransactionService(actualTransactionService);
            Reminder entity = editForm.toEntity();

            // Save entity.
            Reminder save = service.save(entity);
            String successMessage = "Reminder successfully added.";
            if (id != null) {
                successMessage = "Reminder successfully updated.";
            }
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            redirectAttributes.addAttribute("id", save.getId());
            return "redirect:/reminder?id={id}";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reminder was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reminder was updated by another user.");
            return "redirect:/";
        }
    }

    @PostMapping("/reminderDelete")
    public String processReminderDelete(@RequestParam("id") Long id, @RequestParam("version") Integer version,
            RedirectAttributes redirectAttributes) {
        try {
            Reminder entity = service.findById(id);
            if (entity.getVersion() == version) {
                service.delete(entity);
                String successMessage = "Reminder successfully deleted.";
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
                return "redirect:/reminderList";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Reminder was updated by another user.");
                return "redirect:/";
            }
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reminder was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reminder was updated by another user.");
            return "redirect:/";
        } catch (ReferentialIntegrityException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reminder cannot be deleted because other data depends on it.");
            return "redirect:/";
        }
    }

    @ModelAttribute("allRecurringSchedule")
    public List<EntitySelectOption> loadRecurringScheduleSelectOptions() {
        List<EntitySelectOption> options = new ArrayList<>();
        for (RecurringSchedule entity : recurringScheduleService.findAll()) {
            EntitySelectOption option = new EntitySelectOption(entity.getId(), entity.toString());
            options.add(option);
        }
        return options;
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

    @ModelAttribute("allActualTransaction")
    public List<EntitySelectOption> loadActualTransactionSelectOptions(Long parentId) {
        List<EntitySelectOption> options = new ArrayList<>();
        for (Transaction entity : actualTransactionService.findAll(parentId)) {
            EntitySelectOption option = new EntitySelectOption(entity.getId(), entity.toString());
            options.add(option);
        }
        return options;
    }
}
