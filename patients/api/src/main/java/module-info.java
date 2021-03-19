module mediscreen.patients.api {
    requires static java.desktop; // for lombok.anyConstructor.addConstructorProperties
    requires static java.validation;
    requires static lombok;

    requires mediscreen.common.api;

    exports com.mediscreen.patients.api.model;
}