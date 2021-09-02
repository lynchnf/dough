package norman.dough.domain.repository;

import norman.dough.domain.Reminder;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReminderRepository extends PagingAndSortingRepository<Reminder, Long> {
}
