package norman.dough.domain.repository;

import norman.dough.domain.RecurringSchedule;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecurringScheduleRepository extends PagingAndSortingRepository<RecurringSchedule, Long> {
}
