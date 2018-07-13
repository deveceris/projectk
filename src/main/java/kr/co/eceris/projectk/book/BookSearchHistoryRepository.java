package kr.co.eceris.projectk.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSearchHistoryRepository extends CrudRepository<BookSearchHistory, Long> {

    List<BookSearchHistory> findTop10ByUserIdOrderByIdDesc(Long userId);

    BookSearchHistory findByUserIdAndKeyword(Long userId, String keyword);

}
