package kr.co.eceris.projectk.book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class BookConnector {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    @Value("${kakao.api.url}")
    private String apiUrl;

    @Value("${kakao.api.key}")
    private String apiKey;


    /**
     * @param query    / 검색을 원하는 질의어	/ 필수 / String
     * @param sort     / 결과 문서 정렬 방식 / 비필수 / accuracy (정확도순) or recency (최신순) or sales (판매량순)
     * @param page     / 결과 페이지 번호	/ 비필수 / 1-50 사이 Integer
     * @param size     / 한 페이지에 보여질 문서의 개수 / 비필수 /
     * @param target   / 검색 필드 제한 / 비필수 /
     * @param category / 카테고리 필터링 / 비필수 / Integer.
     */
//    https://dapi.kakao.com/v2/search/book?query=개미&page=1&size=10
//    query, sort, page, size, target, category
    public void search(String query, String sort, int page, int size, String target, int category) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("query", query);
        parameters.add("sort", sort);
        parameters.add("page", String.valueOf(page));
        parameters.add("size", String.valueOf(size));
        parameters.add("target", target);
        parameters.add("category", String.valueOf(category));
        ResponseEntity<String> exchange = REST_TEMPLATE.exchange(apiUrl, HttpMethod.GET, new HttpEntity<String>(getHeadersWithKey()), String.class, parameters);

    }

    HttpHeaders getHeadersWithKey() {
        HttpHeaders authorization = new HttpHeaders() {{
            set("Authorization", "KakaoAK " + apiKey);
        }};
        return authorization;
    }


}
