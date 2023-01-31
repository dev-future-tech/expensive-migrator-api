package com.example.expensivemigratorapi;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContainerCallbackExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    public final Logger log = LoggerFactory.getLogger(ContainerCallbackExtension.class);
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        log.info("********** Running the beforeAll method of the ContainerCallbackExtension **********");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        log.debug("********** Running this after all the tests have been completed **********");
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        log.info("********** Running the beforeEach method of the ContainerCallbackExtension **********");
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        log.info("********** Running the afterEach method of the ContainerCallbackExtension **********");

    }
}
