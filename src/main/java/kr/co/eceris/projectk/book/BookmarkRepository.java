package kr.co.eceris.projectk.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {

    List<Bookmark> findByIdIsNotNull();
    List<Bookmark> findByUserId(Long userId);
}
