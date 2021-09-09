package norman.dough.web;

import norman.dough.domain.Account;
import norman.dough.domain.AccountNumber;
import norman.dough.domain.Category;
import norman.dough.exception.InconceivableException;
import norman.dough.exception.NotFoundException;
import norman.dough.exception.OptimisticLockingException;
import norman.dough.service.AccountNumberService;
import norman.dough.service.AccountService;
import norman.dough.service.CategoryService;
import norman.dough.web.view.AccountEditForm;
import norman.dough.web.view.AccountListForm;
import norman.dough.web.view.AccountView;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private static final String defaultSortColumn = "id";
    private static final String[] sortableColumns = {"name", "type", "defaultCategory", "active"};
    @Autowired
    private AccountService service;
    @Autowired
    private CategoryService defaultCategoryService;
    @Autowired
    private AccountNumberService accountNumberService;

    @GetMapping("/accountList")
    public String loadAccountList(
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
        Page<Account> page = service.findAll(pageable);

        // Display the page of records.
        AccountListForm listForm = new AccountListForm(page);
        model.addAttribute("listForm", listForm);
        return "accountList";
    }

    @GetMapping("/account")
    public String loadAccountView(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Account entity = service.findById(id);
            AccountNumber currentAccountNumber = accountNumberService.findCurrentByAccountId(entity.getId());
            AccountView view = new AccountView(entity, currentAccountNumber);
            model.addAttribute("view", view);
            return "accountView";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Account was not found.");
            return "redirect:/";
        } catch (InconceivableException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected Error! See log for details.");
            return "redirect:/";
        }
    }

    @GetMapping("/accountEdit")
    public String loadAccountEdit(@RequestParam(value = "id", required = false) Long id, Model model,
            RedirectAttributes redirectAttributes) {

        // If no id, add new record.
        if (id == null) {
            model.addAttribute("editForm", new AccountEditForm());
            return "accountEdit";
        } else {

            // Otherwise, edit existing record.
            try {
                Account entity = service.findById(id);
                AccountNumber currentAccountNumber = accountNumberService.findCurrentByAccountId(entity.getId());
                AccountEditForm editForm = new AccountEditForm(entity, currentAccountNumber);
                model.addAttribute("editForm", editForm);
                return "accountEdit";
            } catch (NotFoundException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Account was not found.");
                return "redirect:/";
            } catch (InconceivableException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Unexpected Error! See log for details.");
                return "redirect:/";
            }
        }
    }

    @PostMapping("/accountEdit")
    public String processAccountEdit(@Valid @ModelAttribute("editForm") AccountEditForm editForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "accountEdit";
        }

        // Convert form to entity.
        Long id = editForm.getId();
        try {
            editForm.setDefaultCategoryService(defaultCategoryService);
            Account entity = editForm.toEntity();
            AccountNumber accountNumber = null;
            BigDecimal amount = null;

            // If new account or account number changed or effective date changed, we need to save account number.
            if (id == null || !editForm.getNumber().equals(editForm.getOldNumber()) ||
                    !editForm.getEffectiveDate().equals(editForm.getOldEffectiveDate())) {
                accountNumber = editForm.toAccountNumber();
            }

            // If new account, we need to save the amount.
            if (id == null) {
                amount = editForm.getAmount();
            }

            // Save entity.
            Account save = service.saveAccount(entity, accountNumber, amount);
            String successMessage = "Account successfully added.";
            if (id != null) {
                successMessage = "Account successfully updated.";
            }
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            redirectAttributes.addAttribute("id", save.getId());
            return "redirect:/account?id={id}";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Account was not found.");
            return "redirect:/";
        } catch (OptimisticLockingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Account was updated by another user.");
            return "redirect:/";
        }
    }

    @ModelAttribute("allDefaultCategory")
    public List<EntitySelectOption> loadDefaultCategorySelectOptions() {
        List<EntitySelectOption> options = new ArrayList<>();
        for (Category entity : defaultCategoryService.findAll()) {
            EntitySelectOption option = new EntitySelectOption(entity.getId(), entity.toString());
            options.add(option);
        }
        return options;
    }
}
