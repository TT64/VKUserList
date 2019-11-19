package app.line.good.androidtestapp;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import app.line.good.androidtestapp.mvp.SearchActivityContract;
import app.line.good.androidtestapp.mvp.SearchActivityPresenter;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class SearchActivityPresenterTest {

    @Mock
    SearchActivityContract.Model model;
    @Mock
    SearchActivityContract.View view;

    SearchActivityPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new SearchActivityPresenter(model);
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
    public void testSearchUserRequest() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
        presenter.searchUserRequest("q", 20, 0);
        Mockito.verify(model, Mockito.times(1)).searchUser("q", 20, 0, presenter);
    }

    @Test
    public void testOnErrorRequestListener() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
        presenter.onErrorRequestListener();
        Mockito.verify(view, Mockito.times(1)).onErrorResponseSearchUserRequest();
    }

}
