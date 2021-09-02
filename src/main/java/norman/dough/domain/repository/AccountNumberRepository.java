package norman.dough.domain.repository;

import norman.dough.domain.AccountNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountNumberRepository extends PagingAndSortingRepository<AccountNumber, Long> {
    Iterable<AccountNumber> findByAccount_Id(Long parentId, Sort sort);

    Page<AccountNumber> findByAccount_Id(Long parentId, Pageable pageable);
}
