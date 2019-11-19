package app.line.good.androidtestapp;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import app.line.good.androidtestapp.mvp.MyFriendsActivityContract;
import app.line.good.androidtestapp.mvp.MyFriendsActivityPresenter;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class MyFriendsActivityPresenterTest {

    @Mock
    JSONObject response;
    @Mock
    MyFriendsActivityContract.Model model;
    @Mock
    MyFriendsActivityContract.View view;

    MyFriendsActivityPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new MyFriendsActivityPresenter(model);
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
    public void testGetFriendsRequest() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
        presenter.getFriendsRequest(20, 0);
        Mockito.verify(model, Mockito.times(1)).getFriends(20, 0, presenter);
    }

    @Test
    public void testOnErrorRequestListener() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
        presenter.onErrorRequestListener();
        Mockito.verify(view, Mockito.times(1)).onErrorResponseGetFriendsRequest();
    }

}
