package com.christiangp.rxjavaplayground;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

public class MigrateExampleTest {

    private MigrateExample                                migrateExample;

    @Mock
    private MigrateExample.DataSourceV1                   v1;

    @Mock
    private MigrateExample.DataSourceV2                   v2;

    @Mock
    private MigrateExample.SearchDB                       searchV1_1;

    @Mock
    private MigrateExample.SearchDB                       searchV1_2;

    @Mock
    private MigrateExample.SavedSearch                    searchV2_1;

    @Captor
    private ArgumentCaptor<List<MigrateExample.SearchDB>> listCaptor;

    @Before
    public void setUp()
            throws Exception {
        MockitoAnnotations.initMocks(this);
        given(v2.save(any())).willReturn(Completable.complete());
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
                .save(listCaptor.capture());
        assertThat(listCaptor.getValue()).containsExactly(searchV1_1, searchV1_2);
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


}