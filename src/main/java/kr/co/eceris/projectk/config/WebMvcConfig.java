package kr.co.eceris.projectk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    public static final String API_VERSION_PREFIX = "api/v";

    /**
     * //API 버전관리를 위해 RequestMappingHandlerMapping 구현(order=0)
     *
     * @return customCondition을 구현한 구현체
     */
    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping(API_VERSION_PREFIX);
    }

    /**
     * resource 핸들링을 위한 설정(swagger 포함)
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**")
                .addResourceLocations("/");
    }

    /**
     * view resolver에 view등록
     *
     * @param registry
     */
    @Override
    public void configureViewResolvers(final ViewResolverRegistry registry) {
        registry.jsp("/", ".html");
    }
}
