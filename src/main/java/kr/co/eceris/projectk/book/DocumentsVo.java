package kr.co.eceris.projectk.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentsVo {
    private List<BookVo> documents;
    private boolean is_end;
    private int pageable_count;
    private int total_count;
}
