package com.christiangp.rxjavaplayground;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public class MigrateExample {

    private final DataSourceV1 dataSourceV1;

    private final DataSourceV2 dataSourceV2;

    public MigrateExample(DataSourceV1 dataSourceV1, DataSourceV2 dataSourceV2) {
        this.dataSourceV1 = dataSourceV1;
        this.dataSourceV2 = dataSourceV2;
    }

    public Completable migrate() {
        return dataSourceV2.getAll()
                           .filter(searchesV2 -> !searchesV2.isEmpty())
                           .switchIfEmpty(dataSourceV1.fetchAllSearchesV1()
                                                      .flatMapObservable(Observable::fromIterable)
                                                      .doOnNext(dataSourceV2::save)
                                                      .ignoreElements()
                                                      .toMaybe())
                           .ignoreElement();
    }

    public Completable migrate2() {
        return dataSourceV2.getAll()
                           .filter(searchesV2 -> !searchesV2.isEmpty())
                           .switchIfEmpty(dataSourceV1.fetchAllSearchesV1()
                                                      .doOnSuccess(searchDBs -> {
                                                          for (SearchDB searchDB : searchDBs) {
                                                              dataSourceV2.save(searchDB);
                                                          }
                                                      })
                                                      .ignoreElement()
                                                      .toMaybe()
                           )
                           .ignoreElement();
    }

    public interface DataSourceV2 {

        Maybe<List<SavedSearch>> getAll();

        void save(SearchDB searchDB);
    }

    public interface DataSourceV1 {

        Maybe<List<SearchDB>> fetchAllSearchesV1();
    }

    public static class SearchDB {

    }

    public static class SavedSearch {

    }
}
