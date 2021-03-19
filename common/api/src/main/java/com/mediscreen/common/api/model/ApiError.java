package com.mediscreen.common.api.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.With;

/**
 * An error returned by the API.
 * <p>
 * Read endpoint documentations for information about possibles {@code code} and {@code metadata}.
 */
@Builder(builderClassName = "Builder", toBuilder = true)
@Value
@With
@AllArgsConstructor(onConstructor = @__(@Deprecated)) // intended to be used only by tools (MapStruct, Jackson, etc)
public class ApiError {
    /**
     * The type of error, indicating who is responsible (if known) for a failed request.
     */
    @lombok.Builder.Default ApiErrorType type = ApiErrorType.UNKNOWN;

    /**
     * The error code, allowing to identify its cause and to manage it correctly.
     */
    String code;

    /**
     * A message to aiding developers to debug.
     */
    String message;

    /**
     * Some metadata about the error allowing to identify its cause in depth.
     */
    @Singular("metadata")
    Map<String, Object> metadata;
}
