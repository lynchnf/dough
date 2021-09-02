package norman.dough.service;

import norman.dough.domain.Statement;
import norman.dough.domain.repository.StatementRepository;
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
public class StatementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementService.class);
    @Autowired
    private StatementRepository repository;

    public Iterable<Statement> findAll(Long parentId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "closeDate", "id");
        return repository.findByAccount_Id(parentId, sort);
    }

    public Page<Statement> findAll(Long parentId, Pageable pageable) {
        return repository.findByAccount_Id(parentId, pageable);
    }

    public Statement findById(Long id) throws NotFoundException {
        Optional<Statement> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException(LOGGER, "Statement", id);
        }
        return optional.get();
    }

    public Statement save(Statement entity) throws OptimisticLockingException {
        try {
            return repository.save(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Statement", entity.getId(), e);
        }
    }

    public void delete(Statement entity) throws OptimisticLockingException, ReferentialIntegrityException {
        try {
            repository.delete(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Statement", entity.getId(), e);
        } catch (DataIntegrityViolationException e) {
            throw new ReferentialIntegrityException(LOGGER, "Statement", entity.getId(), e);

        }
    }
}
