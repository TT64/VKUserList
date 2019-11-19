package app.line.good.androidtestapp.mvp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserFriendsActivityPresenter implements UserFriendsActivityContract.Presenter, UserFriendsActivityContract.Model.OnFinishRequestListener {

    private UserFriendsActivityContract.Model mModel;
    public UserFriendsActivityContract.View mView;

    public UserFriendsActivityPresenter(UserFriendsActivityContract.Model model) {
        this.mModel = model;
    }

    @Override
    public void attachView(UserFriendsActivityContract.View view) {
        this.mView = view;
    }

    @Override
    public void getUserFriendsRequest(int id, int count, int offset) {
        mModel.getUserFriends(id, count, offset, this);
    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void onSuccessRequestListener(JSONObject response) {
        if (mView != null) {
            try {
                JSONArray jsonArray = response.getJSONArray("items");
                List<UserModel> responseUserList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    UserModel currentUser = new UserModel(object.getString("first_name"), object.getString("last_name"));
                    currentUser.setPhoto_50(object.getString("photo_50"));
                    responseUserList.add(currentUser);
                }
                mView.onSuccessResponseGetUserFriendsRequest();
                mView.setDataToRecyclerView(responseUserList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorRequestListener() {
        if (mView != null) {
            mView.onErrorResponseGetUserFriendsRequest();
        }
    }

    @Override
    public void onErrorClosedProfileRequestListener() {
        if (mView != null) {
            mView.onErrorClosedProfileResponseGetUserFriendsRequest();
        }
    }

    @Override
    public void onErrorIncorrectIdRequestListener() {
        if (mView != null) {
            mView.onErrorIncorrectIdResponseGetUserFriendsRequest();
        }
    }
}
