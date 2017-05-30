package com.christiangp.rxjavaplayground;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static org.mockito.BDDMockito.given;

public class CoverageExampleTest {

    private CoverageExample         coverageExample;

    @Mock
    private CoverageExample.Service service;

    @Before
    public void setUp()
            throws Exception {
        MockitoAnnotations.initMocks(this);
        coverageExample = new CoverageExample(service);
    }

    @Test
    public void testSuccess()
            throws Exception {
        given(service.call()).willReturn(Single.just(CoverageExample.Model.success()));

        coverageExample.execute()
                       .test()
                       .assertNoErrors()
                       .assertValue(model -> "success".equals(model.value()));
    }
}