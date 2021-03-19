package com.mediscreen.common.spring.openapi;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * Replace the default "* /*" content type by "application/json".
 */
@Component
@Order(1000)
public class ContentTypeCustomizer implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        operation.getResponses().values().forEach(response -> {
            Content content = response.getContent();
            if (content != null) {
                MediaType mediaType = content.remove("*/*");
                if (mediaType != null) {
                    content.addMediaType("application/json", mediaType);
                }
            }
        });
        return operation;
    }
}
