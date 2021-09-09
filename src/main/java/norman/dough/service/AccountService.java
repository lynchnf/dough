package norman.dough.service;

import norman.dough.domain.Account;
import norman.dough.domain.AccountNumber;
import norman.dough.domain.Statement;
import norman.dough.domain.Transaction;
import norman.dough.domain.repository.AccountNumberRepository;
import norman.dough.domain.repository.AccountRepository;
import norman.dough.domain.repository.StatementRepository;
import norman.dough.domain.repository.TransactionRepository;
import norman.dough.exception.NotFoundException;
import norman.dough.exception.OptimisticLockingException;
import norman.dough.util.MiscUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private static final String BEGIN_TRANSACTION_NAME = "Beginning Balance";
    @Autowired
    private AccountRepository repository;
    @Autowired
    private AccountNumberRepository accountNumberRepository;
    @Autowired
    private StatementRepository statementRepository;
    @Autowired
    private TransactionRepository transactionRepository;

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
    public Account saveAccount(Account account, AccountNumber accountNumber, BigDecimal amount)
            throws OptimisticLockingException {
        try {
            Account savedAccount = repository.save(account);
            if (accountNumber != null) {
                accountNumber.setAccount(savedAccount);
                AccountNumber savedAccountNumber = accountNumberRepository.save(accountNumber);
                if (amount != null) {
                    Statement currentStatement = new Statement();
                    currentStatement.setAccount(savedAccount);
                    currentStatement.setCloseDate(MiscUtils.getEndOfTime());
                    Statement savedCurrentStatement = statementRepository.save(currentStatement);

                    Statement beginStatement = new Statement();
                    beginStatement.setAccount(savedAccount);
                    beginStatement.setCloseDate(accountNumber.getEffectiveDate());
                    beginStatement.setCloseBalance(amount);
                    Statement savedBeginStatement = statementRepository.save(beginStatement);

                    Transaction beginTransaction = new Transaction();
                    beginTransaction.setStatement(savedBeginStatement);
                    beginTransaction.setName(BEGIN_TRANSACTION_NAME);
                    beginTransaction.setPostDate(accountNumber.getEffectiveDate());
                    beginTransaction.setAmount(amount);
                    beginTransaction.setVoided(false);
                    Transaction savedBeginTransaction = transactionRepository.save(beginTransaction);
                }
            }
            return savedAccount;
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Account", account.getId(), e);
        }
    }
}
