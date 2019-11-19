package app.line.good.androidtestapp.mvp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MyFriendsActivityContract {
    public interface View {

        void onSuccessResponseGetFriendsRequest();

        void setDataToRecyclerView(List<UserModel> response);

        void onErrorResponseGetFriendsRequest();
    }

    public interface Presenter {
        void attachView(MyFriendsActivityContract.View view);

        void getFriendsRequest(int count, int offset);

        void destroy();
    }

    public interface Model {
        interface OnFinishRequestListener {

            void onSuccessRequestListener(JSONObject response);

            void onErrorRequestListener();

        }

        void getFriends(int count, int offset, OnFinishRequestListener onFinishRequestListener);

    }
}
