package norman.dough.domain.repository;

import norman.dough.domain.Statement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StatementRepository extends PagingAndSortingRepository<Statement, Long> {
    Iterable<Statement> findByAccount_Id(Long parentId, Sort sort);

    Page<Statement> findByAccount_Id(Long parentId, Pageable pageable);
}
