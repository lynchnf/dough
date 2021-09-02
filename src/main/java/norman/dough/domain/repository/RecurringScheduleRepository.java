package norman.dough.domain.repository;

import norman.dough.domain.RecurringSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecurringScheduleRepository extends PagingAndSortingRepository<RecurringSchedule, Long> {
}
