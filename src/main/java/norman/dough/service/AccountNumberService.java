package norman.dough.service;

import norman.dough.domain.AccountNumber;
import norman.dough.domain.repository.AccountNumberRepository;
import norman.dough.exception.InconceivableException;
import norman.dough.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountNumberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountNumberService.class);
    @Autowired
    private AccountNumberRepository repository;

    public Iterable<AccountNumber> findAll(Long parentId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "effectiveDate", "id");
        return repository.findByAccount_Id(parentId, sort);
    }

    public Page<AccountNumber> findAll(Long parentId, Pageable pageable) {
        return repository.findByAccount_Id(parentId, pageable);
    }

    public AccountNumber findById(Long id) throws NotFoundException {
        Optional<AccountNumber> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException(LOGGER, "Account Number", id);
        }
        return optional.get();
    }

    public AccountNumber findCurrentByAccountId(Long accountId) throws InconceivableException {
        List<AccountNumber> accountNumberList = repository.findTopByAccount_IdOrderByEffectiveDateDesc(accountId);
        // This should never happen. If we have an account, we should have at least one account number.
        if (accountNumberList.isEmpty()) {
            throw new InconceivableException(LOGGER,
                    String.format("No Account Numbers found for accountId=%d.", accountId));
        }
        return accountNumberList.iterator().next();
    }
}
