package norman.dough.web;

import norman.dough.domain.Statement;
import norman.dough.domain.Account;
import norman.dough.exception.NotFoundException;
import norman.dough.exception.OptimisticLockingException;
import norman.dough.exception.ReferentialIntegrityException;
import norman.dough.service.StatementService;
import norman.dough.service.AccountService;
import norman.dough.web.view.StatementEditForm;
import norman.dough.web.view.StatementListForm;
import norman.dough.web.view.StatementView;
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
public class StatementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementController.class);
    private static final String defaultSortColumn = "id";
    private static final String[] sortableColumns = {"closeDate", "closeBalance", "minPayment", "dueDate"};
    @Autowired
    private StatementService service;
    @Autowired
    private AccountService accountService;

    @GetMapping("/statementList")
    public String loadStatementList(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortColumn", required = false, defaultValue = "closeDate") String sortColumn,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(value = "parentId") Long parentId, Model model) {

        // Convert sort column from string to an array of strings.
        String[] sortColumns = {defaultSortColumn};
        if (Arrays.asList(sortableColumns).contains(sortColumn)) {
            sortColumns = new String[]{sortColumn, defaultSortColumn};
        }

        // Get a page of records.
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sortDirection, sortColumns);
        Page<Statement> page = service.findAll(parentId, pageable);

        // Display the page of records.
        StatementListForm listForm = new StatementListForm(page);
        listForm.setParentId(parentId);
        model.addAttribute("listForm", listForm);
        return "statementList";
    }

    @GetMapping("/statement")
    public String loadStatementView(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Statement entity = service.findById(id);
            StatementView view = new StatementView(entity);
            model.addAttribute("view", view);
            return "statementView";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Statement was not found.");
            return "redirect:/";
        }
    }

    @GetMapping("/statementEdit")
    public String loadStatementEdit(@RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "parentId", required = false) Long parentId,
            Model model, RedirectAttributes redirectAttributes) {

        // If no id, add new record.
        if (id == null) {
            try {
                Account parent = accountService.findById(parentId);
                StatementEditForm editForm = new StatementEditForm(parent);
                model.addAttribute("editForm", editForm);
                return "statementEdit";
            } catch (NotFoundException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Account was not found.");
                return "redirect:/";
            }
        } else {

            // Otherwise, edit existing record.
            try {
                Statement entity = service.findById(id);
                StatementEditForm editForm = new StatementEditForm(entity);
                model.addAttribute("editForm", editForm);
                return "statementEdit";
            } catch (NotFoundException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Statement was not found.");
                return "redirect:/";
            }
        }
    }

    @PostMapping("/statementEdit")
    public String processStatementEdit(@Valid @ModelAttribute("editForm") StatementEditForm editForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "statementEdit";
        }

        // Convert form to entity.
        Long id = editForm.getId();
        try {
            editForm.setAccountService(accountService);
            Statement entity = editForm.toEntity();

            // Save entity.
            Statement save = service.save(entity);
            String successMessage = "Statement successfully added.";
            if (id != null) {
                successMessage = "Statement successfully updated.";
            }
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            redirectAttributes.addAttribute("id", save.getId());
            return "redirect:/statement?id={id}";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Statement was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Statement was updated by another user.");
            return "redirect:/";
        }
    }

    @PostMapping("/statementDelete")
    public String processStatementDelete(@RequestParam("id") Long id, @RequestParam("version") Integer version,
            RedirectAttributes redirectAttributes) {
        try {
            Statement entity = service.findById(id);
            if (entity.getVersion() == version) {
                Long parentId = entity.getAccount().getId();
                service.delete(entity);
                String successMessage = "Statement successfully deleted.";
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
                redirectAttributes.addAttribute("parentId", parentId);
                return "redirect:/statementList?parentId={parentId}";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Statement was updated by another user.");
                return "redirect:/";
            }
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Statement was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Statement was updated by another user.");
            return "redirect:/";
        } catch (ReferentialIntegrityException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Statement cannot be deleted because other data depends on it.");
            return "redirect:/";
        }
    }

    @ModelAttribute("allAccount")
    public List<EntitySelectOption> loadAccountSelectOptions() {
        List<EntitySelectOption> options = new ArrayList<>();
        for (Account entity : accountService.findAll()) {
            EntitySelectOption option = new EntitySelectOption(entity.getId(), entity.toString());
            options.add(option);
        }
        return options;
    }
}
