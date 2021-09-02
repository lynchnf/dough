package norman.dough.service;

import norman.dough.domain.Transaction;
import norman.dough.domain.repository.TransactionRepository;
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
public class TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    @Autowired
    private TransactionRepository repository;

    public Iterable<Transaction> findAll(Long parentId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "postDate", "id");
        return repository.findByStatement_Id(parentId, sort);
    }

    public Page<Transaction> findAll(Long parentId, Pageable pageable) {
        return repository.findByStatement_Id(parentId, pageable);
    }

    public Transaction findById(Long id) throws NotFoundException {
        Optional<Transaction> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException(LOGGER, "Transaction", id);
        }
        return optional.get();
    }

    public Transaction save(Transaction entity) throws OptimisticLockingException {
        try {
            return repository.save(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Transaction", entity.getId(), e);
        }
    }

    public void delete(Transaction entity) throws OptimisticLockingException, ReferentialIntegrityException {
        try {
            repository.delete(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Transaction", entity.getId(), e);
        } catch (DataIntegrityViolationException e) {
            throw new ReferentialIntegrityException(LOGGER, "Transaction", entity.getId(), e);

        }
    }
}
