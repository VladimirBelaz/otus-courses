package ru.otus.steps;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.inject.Inject;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.restassured.path.json.JsonPath;
import ru.otus.config.TestModule;
import ru.otus.helpers.HttpHelper;
import ru.otus.stub.StubServer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ApiSteps {

    private StubServer server;
    private String response;

    @Inject
    private HttpHelper helper;   // теперь внедряется

    public ApiSteps() {
        Injector injector = Guice.createInjector(new TestModule());
        injector.injectMembers(this);
    }

    @Before
    public void before() {
        server = new StubServer();
    }

    @After
    public void after() {
        server.stop();
    }

    @Допустим("Stub-сервер запущен")
    public void stubServerIsRunning() {
        server.start();
    }

    @Когда("я отправляю запрос {string}")
    public void request(String endpoint) {
        response = helper.get("http://localhost:8080" + endpoint);
    }

    @Тогда("поле {string} существует")
    public void fieldShouldExist(String field) {
        JsonPath json = new JsonPath(response);
        assertNotNull(json.get(field));
    }

    @Тогда("ответ содержит {string}")
    public void responseShouldContain(String text) {
        assertTrue(response.contains(text));
    }

}