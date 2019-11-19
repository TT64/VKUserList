package app.line.good.androidtestapp.mvp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.VisibleForTesting;

public class SearchActivityContract {
    public interface View {

        void onSuccessResponseSearchUserRequest();

        void setDataToRecyclerView(List<UserModel> response);

        void onErrorResponseSearchUserRequest();
    }

    public interface Presenter {
        void attachView(SearchActivityContract.View view);

        void searchUserRequest(String query, int count, int offset);

        void destroy();
    }

    public interface Model {
        interface OnFinishRequestListener {

            void onSuccessRequestListener(JSONObject response);

            void onErrorRequestListener();

        }

        void searchUser(String query, int count, int offset, OnFinishRequestListener onFinishRequestListener);

    }
}
