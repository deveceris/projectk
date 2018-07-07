package kr.co.eceris.projectk.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BookApiConnector {
    public static final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${kakao.api.url}")
    private String apiUrl;

    @Value("${kakao.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

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
    public DocumentsVo search(String query, String sort, String page, String size, String target, String category) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("query", query);
        if (Optional.ofNullable(sort).isPresent()) parameters.add("sort", sort);
        if (Optional.ofNullable(page).isPresent()) parameters.add("page", page);
        if (Optional.ofNullable(size).isPresent()) parameters.add("size", size);
        if (Optional.ofNullable(target).isPresent()) parameters.add("target", target);
        if (Optional.ofNullable(category).isPresent()) parameters.add("category", category);

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl).queryParams(parameters).build().toString();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(getHeadersWithKey()), String.class);
        JSONObject eventResult = new JSONObject(responseEntity.getBody());
        JSONArray documents = (JSONArray) eventResult.get("documents");
        JSONObject meta = (JSONObject) eventResult.get("meta");
        DocumentsVo documentsVo = null;
        try {
            List<BookVo> bookVos = mapper.readValue(documents.toString(), new TypeReference<List<BookVo>>() {
            });
            documentsVo = new DocumentsVo(bookVos, Boolean.valueOf(meta.get("is_end").toString()), Integer.valueOf(meta.get("pageable_count").toString()), Integer.valueOf(meta.get("pageable_count").toString()));
            return documentsVo;
        } catch (IOException e) {
            logger.info("Failed to parsing DocumentsVo : {}", e);
        }
        return documentsVo;
    }

    HttpHeaders getHeadersWithKey() {
        HttpHeaders authorization = new HttpHeaders() {{
            set("Authorization", "KakaoAK " + apiKey);
        }};
        return authorization;
    }
}
