package ru.otus.tests;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import ru.otus.config.TestModule;
import ru.otus.helpers.HttpHelper;
import ru.otus.stub.StubServer;

public abstract class BaseTest {

    private static StubServer server;

    @Inject
    protected HttpHelper helper;

    @BeforeAll
    static void beforeAll() {
        server = new StubServer();
        server.start();
    }

    @AfterAll
    static void afterAll() {
        server.stop();
    }

    protected BaseTest() {
        Injector injector = Guice.createInjector(new TestModule());
        injector.injectMembers(this);
    }
}