package ru.otus.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ScoreStub {

    public ScoreStub() {

        registerScore();

    }

    private void registerScore() {

        stubFor(get(urlMatching("/user/get/\\d+"))
                .willReturn(okJson("""
                        {
                          "name":"Test user",
                          "score":78
                        }
                        """)));

    }

}