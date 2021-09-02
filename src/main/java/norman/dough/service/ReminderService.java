package norman.dough.service;

import norman.dough.domain.Reminder;
import norman.dough.domain.repository.ReminderRepository;
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
public class ReminderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderService.class);
    @Autowired
    private ReminderRepository repository;

    public Iterable<Reminder> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "estimatedDate", "id");
        return repository.findAll(sort);
    }

    public Page<Reminder> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Reminder findById(Long id) throws NotFoundException {
        Optional<Reminder> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException(LOGGER, "Reminder", id);
        }
        return optional.get();
    }

    public Reminder save(Reminder entity) throws OptimisticLockingException {
        try {
            return repository.save(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Reminder", entity.getId(), e);
        }
    }

    public void delete(Reminder entity) throws OptimisticLockingException, ReferentialIntegrityException {
        try {
            repository.delete(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Reminder", entity.getId(), e);
        } catch (DataIntegrityViolationException e) {
            throw new ReferentialIntegrityException(LOGGER, "Reminder", entity.getId(), e);
        }
    }
}
