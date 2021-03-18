package com.mediscreen.common.api.model;

/**
 * @see ApiError#getType()
 */
public enum ApiErrorType {
    /**
     * The error came from the client (eg.: invalid authorization, missing parameters) and the request could not be processed.
     */
    CLIENT,

    /**
     * The error comes from the service (eg.: a requested resource is missing) and must be handled by the client.
     * Possible errors are documented for each endpoint.
     */
    SERVICE,

    /**
     * The error is unexpected and of unknown source (eg.: network issue, internal service exception).
     */
    UNKNOWN,
}
