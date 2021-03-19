package com.mediscreen.commin.mapstruct.spi;

import javax.lang.model.element.ExecutableElement;
import org.mapstruct.ap.spi.AccessorNamingStrategy;
import org.mapstruct.ap.spi.DefaultAccessorNamingStrategy;

/**
 * A custom {@link AccessorNamingStrategy} excluding lombok withers.
 */
public class CustomAccessorNamingStrategy extends DefaultAccessorNamingStrategy {
    @Override
    protected boolean isFluentSetter(ExecutableElement method) {
        return !isWitherMethod(method) && super.isFluentSetter(method);
    }

    protected boolean isWitherMethod(ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        return methodName.length() > 4 && methodName.startsWith("with") && Character.isUpperCase(methodName.charAt(4));
    }
}
