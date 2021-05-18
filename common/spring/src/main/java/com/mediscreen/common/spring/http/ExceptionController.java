package com.mediscreen.common.spring.http;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mediscreen.common.api.model.ApiError;
import com.mediscreen.common.api.model.ApiErrorType;
import com.mediscreen.common.spring.validation.PreconditionException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ControllerAdvice
@Slf4j
public class ExceptionController {
    /**
     * Handles missing handlers ("404 errors").
     * <p>
     * Returns a CLIENT/BAD_REQUEST error.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return errorBadRequest(ex.getMessage());
    }

    /**
     * Handles missing handlers ("405 errors").
     * <p>
     * Returns a CLIENT/BAD_REQUEST error.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return errorBadRequest(ex.getMessage());
    }

    /**
     * Handles non-readable requests (eg. invalid json).
     * <p>
     * Returns a CLIENT/BAD_REQUEST error.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (ex.getMessage() != null && ex.getMessage().startsWith("Required request body is missing")) {
            return errorValidationFailed("is required", "body", null, null);
        }
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
            String parameter = cause.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
            Map<String, Object> attributes = new LinkedHashMap<>();
            attributes.put("parserMessage", cause.getOriginalMessage());
            return errorValidationFailed("is badly formatted", parameter, null, attributes);
        }
        return errorBadRequest(ex.getMessage());
    }

    /**
     * Handles missing @RequestParam.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleMissingRequestParamException(MissingServletRequestParameterException ex) {
        String message = "is required";
        String parameter = ex.getParameterName();
        return errorValidationFailed(message, parameter, null, null);
    }

    /**
     * Handles bad types @RequestParam.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "must be a " + ex.getParameter().getParameterType().getSimpleName();
        String parameter = ex.getName();
        return errorValidationFailed(message, parameter, null, null);
    }

    /**
     * Handles failed validation.
     * <p>
     * Returns a CLIENT/VALIDATION_FAILED error.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        ConstraintViolation<?> fieldError = ex.getConstraintViolations().stream().findFirst().get();
        String message = fieldError.getMessage();
        String parameter = StreamSupport.stream(fieldError.getPropertyPath().spliterator(), false)
                .skip(1L).map(Path.Node::toString).collect(Collectors.joining("."));
        String constraint = fieldError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
        Map<String, Object> attributes = fieldError.getConstraintDescriptor().getAttributes();
        return errorValidationFailed(message, parameter, constraint, attributes);
    }

    /**
     * Handles failed validation.
     * <p>
     * Returns a CLIENT/VALIDATION_FAILED error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // Only handles the first FieldError if there is one
        Optional<FieldError> fieldError = ex.getBindingResult()
                .getAllErrors().stream().filter(error -> error instanceof FieldError)
                .map(error -> (FieldError) error).findFirst();
        if (fieldError.isPresent()) {
            String message = fieldError.get().getDefaultMessage();
            String parameter = fieldError.get().getField();
            String constraint = fieldError.get().getCode();
            // OPT: also retrieves attributes
            return errorValidationFailed(message, parameter, constraint, null);
        }

        // Or handles the first misc error
        ObjectError objectError = ex.getBindingResult().getAllErrors().get(0);
        String message = objectError.getDefaultMessage();
        String constraint = objectError.getCode();
        // OPT: also retrieves attributes
        return errorValidationFailed(message, null, constraint, null);
    }

    /**
     * Handles failed validation.
     * <p>
     * Returns a CLIENT/VALIDATION_FAILED error.
     */
    @ExceptionHandler(PreconditionException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handlePreconditionException(PreconditionException ex) {
        return errorValidationFailed(ex.getMessage(), ex.getParameter(), ex.getConstraint(), ex.getAttributes());
    }

    /**
     * Catch all others unhandled exceptions.
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<?> handleOthersException(Throwable ex, ServletWebRequest req) {
        String description = "method=" + req.getHttpMethod() + ";" + req.getDescription(true);
        logger.warn("Unhandled exception (" + description + "):", ex);
        return new ResponseEntity<>("Internal server error (unhandled exception)", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiError> errorBadRequest(String message) {
        return new ResponseEntity<>(ApiError.builder()
                .type(ApiErrorType.CLIENT)
                .code("BAD_REQUEST")
                .message(message)
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiError> errorValidationFailed(String message, String parameter, String constraint, Map<String, Object> attributes) {
        if (parameter != null) {
            message = parameter + " " + message;
        }
        ApiError.Builder res = ApiError.builder()
                .type(ApiErrorType.CLIENT)
                .code("VALIDATION_FAILED")
                .message("Validation failed: " + message);
        if (parameter != null) {
            res.metadata("parameter", parameter);
        }
        if (constraint != null) {
            res.metadata("constraint", constraint);
        }
        if (attributes != null) {
            attributes.forEach((name, value) -> {
                switch (name) {
                    case "groups":
                    case "message":
                    case "payload":
                        break;
                    default:
                        res.metadata(name, value);
                }
            });
        }
        return new ResponseEntity<>(res.build(), HttpStatus.BAD_REQUEST);
    }
}
