package norman.dough.service;

import norman.dough.domain.RecurringSchedule;
import norman.dough.domain.repository.RecurringScheduleRepository;
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
public class RecurringScheduleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecurringScheduleService.class);
    @Autowired
    private RecurringScheduleRepository repository;

    public Iterable<RecurringSchedule> findAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "cronString", "id");
        return repository.findAll(sort);
    }

    public Page<RecurringSchedule> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public RecurringSchedule findById(Long id) throws NotFoundException {
        Optional<RecurringSchedule> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException(LOGGER, "Recurring Schedule", id);
        }
        return optional.get();
    }

    public RecurringSchedule save(RecurringSchedule entity) throws OptimisticLockingException {
        try {
            return repository.save(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Recurring Schedule", entity.getId(), e);
        }
    }

    public void delete(RecurringSchedule entity) throws OptimisticLockingException, ReferentialIntegrityException {
        try {
            repository.delete(entity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingException(LOGGER, "Recurring Schedule", entity.getId(), e);
        } catch (DataIntegrityViolationException e) {
            throw new ReferentialIntegrityException(LOGGER, "Recurring Schedule", entity.getId(), e);

        }
    }
}
