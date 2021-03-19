module mediscreen.common.api {
    requires static java.desktop; // for lombok.anyConstructor.addConstructorProperties
    requires static java.validation;
    requires static lombok;

    exports com.mediscreen.common.api.model;
    exports com.mediscreen.common.api.validation.group;
}