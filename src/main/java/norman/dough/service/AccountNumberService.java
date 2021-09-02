package norman.dough.service;

import norman.dough.domain.AccountNumber;
import norman.dough.domain.repository.AccountNumberRepository;
import norman.dough.exception.NotFoundException;
import norman.dough.exception.OptimisticLockingException;
import norman.dough.exception.ReferentialIntegrityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

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

    public AccountNumber save(AccountNumber entity) throws OptimisticLockingException {
        try {
            return repository.save(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Account Number", entity.getId(), e);
        }
    }

    public void delete(AccountNumber entity) throws OptimisticLockingException, ReferentialIntegrityException {
        try {
            repository.delete(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Account Number", entity.getId(), e);
        } catch (DataIntegrityViolationException e) {
            throw new ReferentialIntegrityException(LOGGER, "Account Number", entity.getId(), e);

        }
    }
}
