package com.christiangp.rxjavaplayground;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class MigrateExample {

    private final DataSourceV1 dataSourceV1;

    private final DataSourceV2 dataSourceV2;

    public MigrateExample(DataSourceV1 dataSourceV1, DataSourceV2 dataSourceV2) {
        this.dataSourceV1 = dataSourceV1;
        this.dataSourceV2 = dataSourceV2;
    }

    public Completable migrate() {
        return dataSourceV2.getAll()
                           .filter(List::isEmpty)
                           .flatMapCompletable(ignoredEmptyList -> dataSourceV1.fetchAllSearchesV1()
                                                                               .flatMapCompletable(dataSourceV2::save));
    }

    public interface DataSourceV2 {

        Single<List<SavedSearch>> getAll();

        Completable save(List<SearchDB> searchDB);
    }

    public interface DataSourceV1 {

        Single<List<SearchDB>> fetchAllSearchesV1();
    }

    public static class SearchDB {

    }

    public static class SavedSearch {

    }
}
