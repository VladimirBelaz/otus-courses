package ru.otus;

import ru.otus.stub.StubServer;

public class Main {

    public static void main(String[] args) throws Exception {

        StubServer server = new StubServer();

        server.start();

        System.out.println("WireMock started");
        System.out.println("http://localhost:8080/user/get/1");

        Thread.currentThread().join();
    }

}