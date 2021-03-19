package com.mediscreen.common.spring.openapi.error;

import com.mediscreen.common.api.model.ApiErrorType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(ApiErrorResponses.class)
public @interface ApiErrorResponse {
    String description() default "";

    String condition() default "";

    ApiErrorType type() default ApiErrorType.UNKNOWN;

    String code() default "";

    String message() default "";

    // TODO: metadata?

    int status() default 0;

    String method() default "";
}
