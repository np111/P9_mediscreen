package com.mediscreen.common.spring.validation;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * JSR-303 {@link ConstraintValidatorFactory} implementation that delegates to a Spring BeanFactory for retrieving
 * or creating autowired ConstraintValidator instances.
 * <p>
 * Unlike the native spring boot implementation, this class will first try to find an existing bean (using
 * {@link BeanFactory#getBean(Class)}).
 * So you can declare your {@linkplain ConstraintValidator validators} as {@linkplain Component components} and avoid
 * cross dependency between DTO and business layers!
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class SpringConstraintValidatorFactoryEx implements ConstraintValidatorFactory {
    private final AutowireCapableBeanFactory beanFactory;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        try {
            T ret = beanFactory.getBean(key);
            if (!(ret instanceof SpringConstraintValidator)) {
                throw new IllegalArgumentException(ret + " must extends " + SpringConstraintValidator.class);
            }
            return ret;
        } catch (BeansException ignored) {
        }
        return (T) new ReleasableConstraintValidator(beanFactory.createBean(key));
    }

    public void releaseInstance(ConstraintValidator<?, ?> instance) {
        if (instance instanceof ReleasableConstraintValidator) {
            beanFactory.destroyBean(((ReleasableConstraintValidator<?, ?>) instance).handle);
        }
    }

    @RequiredArgsConstructor
    private static class ReleasableConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {
        private final ConstraintValidator<A, T> handle;

        @Override
        public void initialize(A annotation) {
            handle.initialize(annotation);
        }

        @Override
        public boolean isValid(T value, ConstraintValidatorContext ctx) {
            return handle.isValid(value, ctx);
        }
    }
}
