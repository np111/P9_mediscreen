module mediscreen.common.spring {
    requires static java.desktop; // for lombok.anyConstructor.addConstructorProperties
    requires static java.validation;
    requires static lombok;
    requires static org.mapstruct;

    requires com.fasterxml.jackson.databind;
    requires io.swagger.v3.oas.models;
    requires javax.servlet.api;
    requires mediscreen.common.api;
    requires mediscreen.common.util;
    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires org.springdoc.openapi.common;
    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.web;
    requires spring.webmvc;

    exports com.mediscreen.common.spring.config;
    exports com.mediscreen.common.spring.http;
    exports com.mediscreen.common.spring.validation;
}