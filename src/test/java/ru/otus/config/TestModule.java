package ru.otus.config;

import com.google.inject.AbstractModule;
import ru.otus.helpers.HttpHelper;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HttpHelper.class).asEagerSingleton();
    }
}