module mediscreen.common.api {
    requires static lombok;
    requires static java.validation;

    exports com.mediscreen.common.api.model;
    exports com.mediscreen.common.api.validation.group;
}