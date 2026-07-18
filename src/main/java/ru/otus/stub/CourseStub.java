package ru.otus.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class CourseStub {

    public CourseStub() {

        registerCourses();

    }

    private void registerCourses() {

        stubFor(get(urlEqualTo("/course/get/all"))
                .willReturn(okJson("""
                        [
                          {
                            "name":"QA java",
                            "price":15000
                          },
                          {
                            "name":"Java",
                            "price":12000
                          }
                        ]
                        """)));

    }

}