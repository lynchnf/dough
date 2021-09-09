package norman.dough.service;

import norman.dough.domain.Account;
import norman.dough.domain.AccountNumber;
import norman.dough.domain.repository.AccountNumberRepository;
import norman.dough.domain.repository.AccountRepository;
import norman.dough.exception.NotFoundException;
import norman.dough.exception.OptimisticLockingException;
import norman.dough.service.response.SaveAccountResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    private AccountRepository repository;
    @Autowired
    private AccountNumberRepository accountNumberRepository;

    public Iterable<Account> findAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name", "id");
        return repository.findAll(sort);
    }

    public Page<Account> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Account findById(Long id) throws NotFoundException {
        Optional<Account> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException(LOGGER, "Account", id);
        }
        return optional.get();
    }

    public Account save(Account entity) throws OptimisticLockingException {
        try {
            return repository.save(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Account", entity.getId(), e);
        }
    }

    @Transactional
    public SaveAccountResponse saveAccount(Account account, AccountNumber accountNumber)
            throws OptimisticLockingException {
        try {
            Account savedAccount = repository.save(account);
            SaveAccountResponse response = new SaveAccountResponse();
            response.setSavedAccount(savedAccount);
            if (accountNumber != null) {
                accountNumber.setAccount(savedAccount);
                AccountNumber savedAccountNumber = accountNumberRepository.save(accountNumber);
                response.setSavedAccountNumber(savedAccountNumber);
            }
            return response;
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Account", account.getId(), e);
        }
    }
}
