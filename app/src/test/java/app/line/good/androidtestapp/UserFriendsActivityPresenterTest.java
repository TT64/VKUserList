package app.line.good.androidtestapp;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import app.line.good.androidtestapp.mvp.UserFriendsActivityContract;
import app.line.good.androidtestapp.mvp.UserFriendsActivityPresenter;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class UserFriendsActivityPresenterTest {

    @Mock
    UserFriendsActivityContract.Model model;
    @Mock
    UserFriendsActivityContract.View view;

    UserFriendsActivityPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new UserFriendsActivityPresenter(model);
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
        presenter.getUserFriendsRequest(1, 20, 0);
        Mockito.verify(model, Mockito.times(1)).getUserFriends(1, 20, 0, presenter);
    }

    @Test
    public void testOnErrorRequestListener() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
        presenter.onErrorRequestListener();
        Mockito.verify(view, Mockito.times(1)).onErrorResponseGetUserFriendsRequest();
    }

    @Test
    public void testOnErrorIncorrectIdRequestListener() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
        presenter.onErrorIncorrectIdRequestListener();
        Mockito.verify(view, Mockito.times(1)).onErrorIncorrectIdResponseGetUserFriendsRequest();
    }

    @Test
    public void testOnErrorClosedProfileRequestListener() {
        assertNull("Must be null", presenter.mView);
        presenter.attachView(view);
        assertNotNull("Must be not null", presenter.mView);
        presenter.onErrorClosedProfileRequestListener();
        Mockito.verify(view, Mockito.times(1)).onErrorClosedProfileResponseGetUserFriendsRequest();
    }
}
