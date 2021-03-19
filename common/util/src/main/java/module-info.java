module mediscreen.common.util {
    requires static java.desktop; // for lombok.anyConstructor.addConstructorProperties
    requires static lombok;

    exports com.mediscreen.common.util;
    exports com.mediscreen.common.util.exception;
}