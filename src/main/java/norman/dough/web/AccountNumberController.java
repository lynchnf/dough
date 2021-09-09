package norman.dough.web;

import norman.dough.domain.Account;
import norman.dough.domain.AccountNumber;
import norman.dough.exception.NotFoundException;
import norman.dough.service.AccountNumberService;
import norman.dough.service.AccountService;
import norman.dough.web.view.AccountNumberListForm;
import norman.dough.web.view.AccountNumberView;
import norman.dough.web.view.EntitySelectOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AccountNumberController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountNumberController.class);
    private static final String defaultSortColumn = "id";
    private static final String[] sortableColumns = {"number", "effectiveDate"};
    @Autowired
    private AccountNumberService service;
    @Autowired
    private AccountService accountService;

    @GetMapping("/accountNumberList")
    public String loadAccountNumberList(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortColumn", required = false, defaultValue = "effectiveDate") String sortColumn,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(value = "parentId") Long parentId, Model model) {

        // Convert sort column from string to an array of strings.
        String[] sortColumns = {defaultSortColumn};
        if (Arrays.asList(sortableColumns).contains(sortColumn)) {
            sortColumns = new String[]{sortColumn, defaultSortColumn};
        }

        // Get a page of records.
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sortDirection, sortColumns);
        Page<AccountNumber> page = service.findAll(parentId, pageable);

        // Display the page of records.
        AccountNumberListForm listForm = new AccountNumberListForm(page);
        listForm.setParentId(parentId);
        model.addAttribute("listForm", listForm);
        return "accountNumberList";
    }

    @GetMapping("/accountNumber")
    public String loadAccountNumberView(@RequestParam("id") Long id, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            AccountNumber entity = service.findById(id);
            AccountNumberView view = new AccountNumberView(entity);
            model.addAttribute("view", view);
            return "accountNumberView";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Account Number was not found.");
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
