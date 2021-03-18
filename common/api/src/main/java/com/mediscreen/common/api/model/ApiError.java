package com.mediscreen.common.api.model;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

/**
 * An error returned by the API.
 * <p>
 * Read endpoint documentations for information about possibles {@code code} and {@code metadata}.
 */
@lombok.Builder(builderClassName = "Builder")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class ApiError {
    /**
     * The type of error, indicating who is responsible (if known) for a failed request.
     */
    private @lombok.Builder.Default ApiErrorType type = ApiErrorType.UNKNOWN;

    /**
     * The error code, allowing to identify its cause and to manage it correctly.
     */
    private String code;

    /**
     * A message to aiding developers to debug.
     */
    private String message;

    /**
     * Some metadata about the error allowing to identify its cause in depth.
     */
    @Singular("metadata")
    private Map<String, Object> metadata;

}
