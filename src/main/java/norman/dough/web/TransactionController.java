package norman.dough.web;

import norman.dough.domain.Category;
import norman.dough.domain.Statement;
import norman.dough.domain.Transaction;
import norman.dough.exception.NotFoundException;
import norman.dough.exception.OptimisticLockingException;
import norman.dough.exception.ReferentialIntegrityException;
import norman.dough.service.CategoryService;
import norman.dough.service.StatementService;
import norman.dough.service.TransactionService;
import norman.dough.web.view.EntitySelectOption;
import norman.dough.web.view.TransactionEditForm;
import norman.dough.web.view.TransactionListForm;
import norman.dough.web.view.TransactionView;
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
public class TransactionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);
    private static final String defaultSortColumn = "id";
    private static final String[] sortableColumns = {"name", "postDate", "amount", "checkNumber", "category", "voided"};
    @Autowired
    private TransactionService service;
    @Autowired
    private StatementService statementService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TransactionService transferService;

    @GetMapping("/transactionList")
    public String loadTransactionList(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortColumn", required = false, defaultValue = "postDate") String sortColumn,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(value = "parentId") Long parentId, Model model) {

        // Convert sort column from string to an array of strings.
        String[] sortColumns = {defaultSortColumn};
        if (Arrays.asList(sortableColumns).contains(sortColumn)) {
            sortColumns = new String[]{sortColumn, defaultSortColumn};
        }

        // Get a page of records.
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sortDirection, sortColumns);
        Page<Transaction> page = service.findAll(parentId, pageable);

        // Display the page of records.
        TransactionListForm listForm = new TransactionListForm(page);
        listForm.setParentId(parentId);
        model.addAttribute("listForm", listForm);
        return "transactionList";
    }

    @GetMapping("/transaction")
    public String loadTransactionView(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Transaction entity = service.findById(id);
            TransactionView view = new TransactionView(entity);
            model.addAttribute("view", view);
            return "transactionView";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Transaction was not found.");
            return "redirect:/";
        }
    }

    @GetMapping("/transactionEdit")
    public String loadTransactionEdit(@RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "parentId", required = false) Long parentId, Model model,
            RedirectAttributes redirectAttributes) {

        // If no id, add new record.
        if (id == null) {
            try {
                Statement parent = statementService.findById(parentId);
                TransactionEditForm editForm = new TransactionEditForm(parent);
                model.addAttribute("editForm", editForm);
                return "transactionEdit";
            } catch (NotFoundException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Statement was not found.");
                return "redirect:/";
            }
        } else {

            // Otherwise, edit existing record.
            try {
                Transaction entity = service.findById(id);
                TransactionEditForm editForm = new TransactionEditForm(entity);
                model.addAttribute("editForm", editForm);
                return "transactionEdit";
            } catch (NotFoundException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Transaction was not found.");
                return "redirect:/";
            }
        }
    }

    @PostMapping("/transactionEdit")
    public String processTransactionEdit(@Valid @ModelAttribute("editForm") TransactionEditForm editForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "transactionEdit";
        }

        // Convert form to entity.
        Long id = editForm.getId();
        try {
            editForm.setStatementService(statementService);
            editForm.setCategoryService(categoryService);
            editForm.setTransferService(transferService);
            Transaction entity = editForm.toEntity();

            // Save entity.
            Transaction save = service.save(entity);
            String successMessage = "Transaction successfully added.";
            if (id != null) {
                successMessage = "Transaction successfully updated.";
            }
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            redirectAttributes.addAttribute("id", save.getId());
            return "redirect:/transaction?id={id}";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Transaction was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Transaction was updated by another user.");
            return "redirect:/";
        }
    }

    @PostMapping("/transactionDelete")
    public String processTransactionDelete(@RequestParam("id") Long id, @RequestParam("version") Integer version,
            RedirectAttributes redirectAttributes) {
        try {
            Transaction entity = service.findById(id);
            if (entity.getVersion() == version) {
                Long parentId = entity.getStatement().getId();
                service.delete(entity);
                String successMessage = "Transaction successfully deleted.";
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
                redirectAttributes.addAttribute("parentId", parentId);
                return "redirect:/transactionList?parentId={parentId}";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Transaction was updated by another user.");
                return "redirect:/";
            }
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Transaction was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Transaction was updated by another user.");
            return "redirect:/";
        } catch (ReferentialIntegrityException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Transaction cannot be deleted because other data depends on it.");
            return "redirect:/";
        }
    }

    @ModelAttribute("allStatement")
    public List<EntitySelectOption> loadStatementSelectOptions(Long parentId) {
        List<EntitySelectOption> options = new ArrayList<>();
        for (Statement entity : statementService.findAll(parentId)) {
            EntitySelectOption option = new EntitySelectOption(entity.getId(), entity.toString());
            options.add(option);
        }
        return options;
    }

    @ModelAttribute("allCategory")
    public List<EntitySelectOption> loadCategorySelectOptions() {
        List<EntitySelectOption> options = new ArrayList<>();
        for (Category entity : categoryService.findAll()) {
            EntitySelectOption option = new EntitySelectOption(entity.getId(), entity.toString());
            options.add(option);
        }
        return options;
    }

    @ModelAttribute("allTransfer")
    public List<EntitySelectOption> loadTransferSelectOptions(Long parentId) {
        List<EntitySelectOption> options = new ArrayList<>();
        for (Transaction entity : transferService.findAll(parentId)) {
            EntitySelectOption option = new EntitySelectOption(entity.getId(), entity.toString());
            options.add(option);
        }
        return options;
    }
}
