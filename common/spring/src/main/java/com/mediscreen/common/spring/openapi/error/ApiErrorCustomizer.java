package com.mediscreen.common.spring.openapi.error;

import com.mediscreen.common.api.model.ApiError;
import com.mediscreen.common.api.model.ApiErrorType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocAnnotationsUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
@Order(2)
public class ApiErrorCustomizer implements OpenApiCustomiser, OperationCustomizer {
    private final BeanFactory beanFactory;

    @Override
    public void customise(OpenAPI openAPI) {
        // register the ApiError schema
        SpringDocAnnotationsUtils.resolveSchemaFromType(ApiError.class, openAPI.getComponents(), null);
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiErrorResponse[] responses = handlerMethod.getMethod().getAnnotationsByType(ApiErrorResponse.class);
        for (ApiErrorResponse response : responses) {
            addApiErrorResponse(operation, handlerMethod, response);
        }
        return operation;
    }

    private void addApiErrorResponse(Operation operation, HandlerMethod handlerMethod, ApiErrorResponse response) {
        ApiError.Builder apiError;
        int httpStatus;
        if (!response.method().isEmpty()) {
            ResponseEntity<ApiError> entity = callApiErrorMethod(handlerMethod, response.method());
            apiError = Objects.requireNonNull(entity.getBody()).toBuilder();
            httpStatus = entity.getStatusCodeValue();
        } else {
            apiError = ApiError.builder();
            httpStatus = HttpStatus.OK.value();
        }
        if (response.type() != ApiErrorType.UNKNOWN) {
            apiError.type(response.type());
        }
        if (!response.code().isEmpty()) {
            apiError.code(response.code());
        }
        if (!response.message().isEmpty()) {
            apiError.message(response.message());
        }
        if (response.status() != 0) {
            httpStatus = response.status();
        }
        addApiErrorResponse(operation, apiError.build(), httpStatus, response.description(), response.condition());
    }

    public void addApiErrorResponse(Operation operation, ApiError apiError, int httpStatus, String description, String condition) {
        String name = "" + httpStatus;
        while (operation.getResponses().containsKey(name)) {
            name += "'";
        }

        if (description == null || description.isEmpty()) {
            description = StringUtils.capitalize(StringUtils.defaultString(apiError.getMessage()));
        }

        description = "`" + apiError.getType() + "`/`" + apiError.getCode() + "` - " + description;
        if (condition != null && !condition.isEmpty()) {
            description = "*if (" + condition + "):*\n>" + description.replace("\n", "\n>");
        }

        operation.getResponses().addApiResponse(name, new ApiResponse()
                .description(description)
                .content(new Content()
                        .addMediaType("*/*", new MediaType()
                                .schema(new Schema<>().$ref("ApiError"))
                                .example(apiError))));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private ResponseEntity<ApiError> callApiErrorMethod(HandlerMethod handlerMethod, String methodName) {
        Method method = findMethod(handlerMethod, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Method not found: " + handlerMethod + " -> " + methodName);
        }

        Object instance = Modifier.isStatic(method.getModifiers()) ? null : beanFactory.getBean(method.getDeclaringClass());

        Object ret = method.invoke(instance, initParameters(method.getParameters()));
        return (ResponseEntity<ApiError>) ret;
    }

    private Method findMethod(HandlerMethod handlerMethod, String methodName) {
        Class<?> clazz = handlerMethod.getMethod().getDeclaringClass();
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .filter(m -> ApiError.class.isAssignableFrom(m.getReturnType())
                        || ResponseEntity.class.isAssignableFrom(m.getReturnType()))
                .min(Comparator.comparingInt(Method::getParameterCount)).orElse(null);
    }

    private static Object[] initParameters(Parameter[] parameters) {
        return Arrays.stream(parameters).map(ApiErrorCustomizer::initParameter).toArray();
    }

    @SneakyThrows
    private static Object initParameter(Parameter parameter) {
        Class<?> parameterType = ClassUtils.primitiveToWrapper(parameter.getType());
        if (Number.class.isAssignableFrom(parameterType)) {
            return parameterType.getConstructor(String.class).newInstance("0");
        }
        if (String.class.isAssignableFrom(parameterType)) {
            return "string";
        }
        if (UUID.class.isAssignableFrom(parameterType)) {
            return new UUID(0, 0);
        }

        Constructor<?> constructor = Arrays.stream(parameterType.getConstructors())
                .min(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new NoSuchMethodException("no constructor for: " + parameter));
        return constructor.newInstance(initParameters(constructor.getParameters()));
    }
}
