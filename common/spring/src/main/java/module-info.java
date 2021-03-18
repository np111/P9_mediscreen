module mediscreen.common.spring {
    requires static lombok;
    requires static java.validation;
    requires static org.mapstruct;

    requires com.fasterxml.jackson.databind;
    requires javax.servlet.api;
    requires mediscreen.common.api;
    requires mediscreen.common.util;
    requires org.slf4j;
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