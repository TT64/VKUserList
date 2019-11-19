package app.line.good.androidtestapp;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import app.line.good.androidtestapp.mvp.PersonalInfoActivityContract;
import app.line.good.androidtestapp.mvp.PersonalInfoActivityPresenter;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class PersonalInfoActivityPresenterTest {

    @Mock
    PersonalInfoActivityContract.Model model;
    @Mock
    PersonalInfoActivityContract.View view;

    PersonalInfoActivityPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new PersonalInfoActivityPresenter(model);

    }

    @Test
    public void testAttachPresenter() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
    }

    @Test
    public void testDetachPresenter() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
        presenter.destroy();
        assertNull("Must be null", presenter.mView);
    }

    @Test
    public void testGetPersonalInfoRequest() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
        presenter.getPersonalInfoRequest();
        Mockito.verify(model, Mockito.times(1)).getPersonalInfo(presenter);
    }

    @Test
    public void testOnErrorRequestListener() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
        presenter.onErrorRequestListener();
        Mockito.verify(view, Mockito.times(1)).onErrorResponseGetPersonalInfoRequest();
    }
}
