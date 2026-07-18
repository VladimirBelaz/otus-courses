package ru.otus.stub;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class StubServer {

    private final WireMockServer server;

    public StubServer() {
        server = new WireMockServer(options().port(8080));
    }

    public void start() {

        server.start();

        configureFor("localhost",8080);

        new UserStub();
        new ScoreStub();
        new CourseStub();

    }

    public void stop() {
        server.stop();
    }

}