package com.christiangp.rxjavaplayground;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

public class MigrateExampleTest {

    private MigrateExample              migrateExample;

    @Mock
    private MigrateExample.DataSourceV1 v1;

    @Mock
    private MigrateExample.DataSourceV2 v2;

    @Mock
    private MigrateExample.SearchDB     searchV1_1;

    @Mock
    private MigrateExample.SearchDB     searchV1_2;

    @Mock
    private MigrateExample.SavedSearch  searchV2_1;

    @Before
    public void setUp()
            throws Exception {
        MockitoAnnotations.initMocks(this);
        migrateExample = new MigrateExample(v1, v2);
    }



    @Test
    public void givenV2HasEmptyElements_WhenMigrate_ThenMigrateFromV1()
            throws Exception {

        given(v2.getAll()).willReturn(Single.just(Collections.emptyList()));
        given(v1.fetchAllSearchesV1()).willReturn(Single.just(Arrays.asList(searchV1_1, searchV1_2)));

        migrateExample.migrate()
                      .test()
                      .assertNoErrors()
                      .assertComplete();

        then(v2).should()
                .save(searchV1_1);
        then(v2).should()
                .save(searchV1_2);
    }

    @Test
    public void givenV2HasItems_WhenMigrate_ThenSaveNothingFromV1()
            throws Exception {
        given(v2.getAll()).willReturn(Single.just(Collections.singletonList(searchV2_1)));
        given(v1.fetchAllSearchesV1()).willReturn(Single.just(Arrays.asList(searchV1_1, searchV1_2)));

        migrateExample.migrate()
                      .test()
                      .assertNoErrors()
                      .assertComplete();

        then(v2).should(never())
                .save(any());
    }



    @Test
    public void givenV2HasEmptyElements_WhenMigrate2_ThenMigrateFromV1()
            throws Exception {

        given(v2.getAll()).willReturn(Single.just(Collections.emptyList()));
        given(v1.fetchAllSearchesV1()).willReturn(Single.just(Arrays.asList(searchV1_1, searchV1_2)));

        migrateExample.migrate2()
                      .test()
                      .assertNoErrors()
                      .assertComplete();

        then(v2).should()
                .save(searchV1_1);
        then(v2).should()
                .save(searchV1_2);
    }

    @Test
    public void givenV2HAsItems_WhenMigrate2_ThenSaveNothingFromV1()
            throws Exception {
        given(v2.getAll()).willReturn(Single.just(Collections.singletonList(searchV2_1)));
        given(v1.fetchAllSearchesV1()).willReturn(Single.just(Arrays.asList(searchV1_1, searchV1_2)));

        migrateExample.migrate2()
                      .test()
                      .assertNoErrors()
                      .assertComplete();

        then(v2).should(never())
                .save(any());
    }
}