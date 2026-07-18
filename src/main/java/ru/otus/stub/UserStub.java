package ru.otus.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class UserStub {

    public UserStub() {

        registerGetUsers();

    }

    private void registerGetUsers() {

        stubFor(get(urlEqualTo("/user/get/all"))
                .willReturn(okJson("""
                        [
                          {
                            "name":"Test user",
                            "course":"QA",
                            "email":"test@test.test",
                            "age":23
                          }
                        ]
                        """)));

    }

}