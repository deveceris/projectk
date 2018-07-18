package kr.co.eceris.projectk.config;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    private final String prefix;

    public ApiVersionRequestMappingHandlerMapping(String prefix) {
        this.prefix = prefix;
    }

    /**
     * method와 type-level에서 RequestMapping을 스캔하여 RequestMappingInfo 재정의
     *
     * @param method
     * @param handlerType
     * @return
     */
    @Override //@RequestMapping을 스캔
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {

        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
        if (info == null) return null;

        //method
        ApiVersion methodAnnotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        if (methodAnnotation != null) {
            RequestCondition<?> methodCondition = getCustomMethodCondition(method);
            // Concatenate our ApiVersion with the usual request mapping
            info = createApiVersionInfo(methodAnnotation, methodCondition).combine(info);
        } else {    //type-level
            ApiVersion typeAnnotation = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
            if (typeAnnotation != null) {
                RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
                // Concatenate our ApiVersion with the usual request mapping
                info = createApiVersionInfo(typeAnnotation, typeCondition).combine(info);
            }
        }

        return info;
    }

    /**
     * ApiVersion annotation에서 버전정보를 받아 매핑 조건을 변경
     * @param annotation
     * @param customCondition
     * @return
     */
    private RequestMappingInfo createApiVersionInfo(ApiVersion annotation, RequestCondition<?> customCondition) {
        int[] values = annotation.value();
        String[] patterns = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            // Build the URL prefix
            patterns[i] = prefix + values[i];
        }

        return new RequestMappingInfo(
                new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()),
                new RequestMethodsRequestCondition(),
                new ParamsRequestCondition(),
                new HeadersRequestCondition(),
                new ConsumesRequestCondition(),
                new ProducesRequestCondition(),
                customCondition);
    }

}

