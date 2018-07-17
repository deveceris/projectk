package kr.co.eceris.projectk.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookSearchHistoryRepository extends CrudRepository<BookSearchHistory, Long> {

    List<BookSearchHistory> findTop10ByUserIdOrderByIdDesc(Long userId);

    Optional<BookSearchHistory> findByUserIdAndKeyword(Long userId, String keyword);

}
