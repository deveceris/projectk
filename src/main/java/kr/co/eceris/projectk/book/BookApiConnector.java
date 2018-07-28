package kr.co.eceris.projectk.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
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

    @Autowired
    private ReactorClientHttpConnector reactorHttpClient;

    /**
     * @param query  / 검색을 원하는 질의어	/ 필수 / String
     * @param page   / 결과 페이지 번호	/ 비필수 / 1-50 사이 Integer
     * @param size   / 한 페이지에 보여질 문서의 개수 / 비필수 /
     * @param target / 검색 필드 제한 / 비필수 / all, title, publisher, isbn
     * @param sort   / 결과 문서 정렬방식 / 비필수 / accuracy (정확도순) or recency (최신순) or sales (판매량순)
     */
//    https://dapi.kakao.com/v2/search/book?query=개미&page=1&size=10
//    query, sort, page, size, target, category
//    https://developers.kakao.com/docs/restapi/search#%EC%B1%85-%EA%B2%80%EC%83%89
    public DocumentsVo search(String query, String page, String size, String target, String sort) {
        String url = toURI(query, page, size, target, sort).toString();

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(getHeadersWithKey()), String.class);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            logger.error("Api call error({}) code:{}, body:{}", url, responseEntity.getStatusCode(), responseEntity.getBody());
            throw new IllegalStateException();
        }
        JSONObject eventResult = new JSONObject(responseEntity.getBody());
        JSONArray documents = (JSONArray) eventResult.get("documents");
        JSONObject meta = (JSONObject) eventResult.get("meta");
        DocumentsVo documentsVo = null;
        try {
            List<BookVo> bookVos = mapper.readValue(documents.toString(), new TypeReference<List<BookVo>>() {
            });
            documentsVo = new DocumentsVo(bookVos, Boolean.valueOf(meta.get("is_end").toString()), Integer.valueOf(meta.get("pageable_count").toString()), Integer.valueOf(meta.get("pageable_count").toString()));
        } catch (IOException e) {
            logger.info("Failed to parsing DocumentsVo : {}", e);
        }
        return documentsVo;
    }

    public Mono<DocumentsVo> nonBlockSearch(String query, String page, String size, String target, String sort) {
        String url = toURI(query, page, size, target, sort).toString();
        Mono<DocumentsVo> documentsVoMono = WebClient.builder().clientConnector(reactorHttpClient)
                .baseUrl(url)
                .defaultHeader("Authorization", "KakaoAK " + apiKey)
                .build()
                .get()
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    logger.error("Api call error({}) code:{}", url, clientResponse.statusCode());
                    return Mono.error(new IllegalArgumentException("code : " + clientResponse.statusCode()));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    logger.error("Api call error({}) code:{}", url, clientResponse.statusCode());
                    return Mono.error(new IllegalStateException("code : " + clientResponse.statusCode()));
                })
                .bodyToMono(String.class)
                .map(body -> {
                    JSONObject eventResult = new JSONObject(body);
                    JSONArray documents = (JSONArray) eventResult.get("documents");
                    JSONObject meta = (JSONObject) eventResult.get("meta");
                    DocumentsVo documentsVo = null;
                    try {
                        List<BookVo> bookVos = mapper.readValue(documents.toString(), new TypeReference<List<BookVo>>() {
                        });
                        documentsVo = new DocumentsVo(bookVos, Boolean.valueOf(meta.get("is_end").toString()), Integer.valueOf(meta.get("pageable_count").toString()), Integer.valueOf(meta.get("pageable_count").toString()));
                    } catch (IOException e) {
                        logger.info("Failed to parsing DocumentsVo : {}", e);
                    }
                    return documentsVo;
                });
        return documentsVoMono;
    }

    HttpHeaders getHeadersWithKey() {
        return new HttpHeaders() {{
            set("Authorization", "KakaoAK " + apiKey);
        }};
    }

    private URI toURI(String query, String page, String size, String target, String sort) {
        return UriComponentsBuilder
                .fromHttpUrl(apiUrl)
                .queryParam("query", Optional.ofNullable(query).isPresent() ? query : null)
                .queryParam("page", Optional.ofNullable(page).isPresent() ? page : null)
                .queryParam("size", Optional.ofNullable(size).isPresent() ? size : null)
                .queryParam("target", Optional.ofNullable(target).isPresent() ? target : null)
                .queryParam("sort", Optional.ofNullable(sort).isPresent() ? sort : null)
                .build()
                .toUri();
    }
}
