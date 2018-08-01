package kr.co.eceris.projectk.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {

    List<Bookmark> findByUserIdOrderByIdDesc(Long userId);
    void deleteByUserId(Long userId);

    //    id, query, page, size, target, sort, isbn, barcode, publisher, title
    Optional<Bookmark> findByUserIdAndQueryAndPageAndSizeAndTargetAndSortAndIsbnAndBarcodeAndPublisherAndTitle(Long userId, String query, int page, int size, String target, String sort, String isbn, String barcode, String publisher, String title);
}
