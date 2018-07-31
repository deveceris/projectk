package kr.co.eceris.projectk;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import kr.co.eceris.projectk.config.ApiVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@EnableTransactionManagement
@EnableSwagger2
@SpringBootApplication
@Controller
public class Application {

    public static final int READ_TIMEOUT = 5000;
    public static final int CONNECT_TIMEOUT = 3000;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Api 문서 제공을 위한 설정(@ApiVersion 만 제공)
     *
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiVersion.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Rest template 제공
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    static HttpComponentsClientHttpRequestFactory httpRequestFactory() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setReadTimeout(READ_TIMEOUT); // 읽기시간초과, ms
        httpComponentsClientHttpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT); // 연결시간초과, ms

        // route(ip 기준)의 상황에 따라 커스터마이징이 필요할 것 같다.
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(200) // max connection
                .setMaxConnPerRoute(200) // route max connection
                .build();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpClient);
        return httpComponentsClientHttpRequestFactory;
    }

    @Bean
    public ReactorClientHttpConnector reactorHttpClient() {
        return new ReactorClientHttpConnector(
                options -> options.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
                        .compression(true)
                        .afterNettyContextInit(ctx -> {
                            ctx.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT, TimeUnit.MILLISECONDS));
                        }));
    }

    @Bean
    public PasswordEncoder pw() {
        return new BCryptPasswordEncoder();
    }

    @GetMapping({"/", "/bookmark", "/search", "/book", "/login", "/signup"})
    public String index() {
        return "index";
    }
}
