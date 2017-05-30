package com.christiangp.rxjavaplayground;

import io.reactivex.Single;

public class CoverageExample {

    private final Service service;

    public CoverageExample(Service service) {
        this.service = service;
    }

    public Single<Model> execute() {
        return service.call()
                      .onErrorReturn(t -> Model.failure());
    }

    public interface Service {

        Single<Model> call();
    }

    public static class Model {

        private final String value;

        private Model(String value) {
            this.value = value;
        }

        public static Model failure() {
            return new Model("failure");
        }

        public static Model success() {
            return new Model("success");
        }

        public String value() {
            return value;
        }
    }
}
