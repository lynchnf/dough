package norman.dough.domain.repository;

import norman.dough.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
    Iterable<Transaction> findByStatement_Id(Long parentId, Sort sort);

    Page<Transaction> findByStatement_Id(Long parentId, Pageable pageable);
}
