package com.mediscreen.common.spring.validation;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A {@link ConstraintValidator} that ensures that {@link ConstraintValidator#initialize(Annotation)} is called only
 * once. It is intended to be implemented as a {@link Component} with a prototype {@link Scope}.
 */
public abstract class SpringConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public final void initialize(A annotation) {
        if (initialized.getAndSet(true)) {
            throw new IllegalStateException("already initialized");
        }
        doInitialize(annotation);
    }

    protected void doInitialize(A annotation) {
    }

    @Override
    public abstract boolean isValid(T value, ConstraintValidatorContext ctx);
}
