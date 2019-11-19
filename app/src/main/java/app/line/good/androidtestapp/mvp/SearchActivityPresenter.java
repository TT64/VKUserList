package app.line.good.androidtestapp.mvp;

import android.util.Log;

import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivityPresenter implements SearchActivityContract.Presenter, SearchActivityContract.Model.OnFinishRequestListener {

    private SearchActivityContract.Model mModel;
    public SearchActivityContract.View mView;

    public SearchActivityPresenter(SearchActivityContract.Model model) {
        this.mModel = model;
    }

    @Override
    public void attachView(SearchActivityContract.View view) {
        this.mView = view;
    }

    @Override
    public void searchUserRequest(String query, int count, int offset) {
        mModel.searchUser(query, count, offset, this);
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
                    if (object.isNull("photo_200"))
                        currentUser.setPhoto_200(object.getString("photo_50"));
                    else
                        currentUser.setPhoto_200(object.getString("photo_200"));
                    if (object.isNull(("relation"))) {
                        currentUser.setRelation(0);
                    } else
                        currentUser.setRelation((object.getInt("relation")));
                    currentUser.setScreenname(object.getString("screen_name"));
                    currentUser.setSex(object.getInt("sex"));
                    currentUser.setId(object.getInt("id"));
                    responseUserList.add(currentUser);
                }
                mView.onSuccessResponseSearchUserRequest();
                mView.setDataToRecyclerView(responseUserList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorRequestListener() {
        if (mView != null) {
            mView.onErrorResponseSearchUserRequest();
        }
    }

}
