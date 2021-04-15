package com.mediscreen.common.spring.openapi.validation;

import com.mediscreen.common.api.model.ApiError;
import com.mediscreen.common.api.model.ApiErrorType;
import com.mediscreen.common.spring.openapi.error.ApiErrorCustomizer;
import io.swagger.v3.oas.models.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.method.HandlerMethod;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
@Order(1)
public class ValidationCustomizer implements OperationCustomizer {
    private final ApiErrorCustomizer apiErrorCustomizer;

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        if (handlerMethod.getMethod().getDeclaringClass().isAnnotationPresent(Validated.class)) {
            List<ConstraintsDescriptor.Description> constraints = ConstraintsDescriptor.describeParameters(handlerMethod.getMethod());
            if (!constraints.isEmpty()) {
                ApiError apiError = ApiError.builder()
                        .type(ApiErrorType.CLIENT)
                        .code("VALIDATION_FAILED")
                        .message("Validation failed: {{message}}")
                        .metadata("parameter", "{{request parameter name}}")
                        .metadata("constraint", "{{constraint class name}}")
                        .metadata("{{constraint attribute}}", "{{value}}")
                        .build();
                StringBuilder description = new StringBuilder();
                description.append("A request parameter validation failed:");
                constraintsToString(description, "", constraints);
                apiErrorCustomizer.addApiErrorResponse(operation, apiError, HttpStatus.BAD_REQUEST.value(), description.toString(), null);
            }
        }
        return operation;
    }

    private void constraintsToString(StringBuilder sb, String prefix, List<ConstraintsDescriptor.Description> descriptions) {
        for (ConstraintsDescriptor.Description description : descriptions) {
            sb.append("\n").append(prefix).append("- *").append(description.getName()).append("*:");
            int i = 0;
            for (String constraint : description.getConstraints()) {
                sb.append(++i == 1 ? " " : ", ");
                sb.append(constraint);
            }
            constraintsToString(sb, prefix + "  ", description.getFields());
        }
    }
}
