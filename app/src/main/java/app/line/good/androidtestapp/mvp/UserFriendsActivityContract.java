package app.line.good.androidtestapp.mvp;

import org.json.JSONObject;

import java.util.List;

public class UserFriendsActivityContract {

    public interface View {

        void onSuccessResponseGetUserFriendsRequest();

        void setDataToRecyclerView(List<UserModel> response);

        void onErrorResponseGetUserFriendsRequest();

        void onErrorClosedProfileResponseGetUserFriendsRequest();

        void onErrorIncorrectIdResponseGetUserFriendsRequest();

    }

    public interface Presenter {
        void attachView(UserFriendsActivityContract.View view);

        void getUserFriendsRequest(int id, int count, int offset);

        void destroy();
    }

    public interface Model {
        interface OnFinishRequestListener {

            void onSuccessRequestListener(JSONObject response);

            void onErrorRequestListener();

            void onErrorClosedProfileRequestListener();

            void onErrorIncorrectIdRequestListener();

        }

        void getUserFriends(int id, int count, int offset, OnFinishRequestListener onFinishRequestListener);

    }
}
