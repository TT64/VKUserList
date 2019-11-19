package app.line.good.androidtestapp.mvp;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import app.line.good.androidtestapp.utils.Constants;

public class PersonalInfoActivityPresenter implements PersonalInfoActivityContract.Presenter, PersonalInfoActivityContract.Model.OnFinishRequestListener {

    public PersonalInfoActivityContract.View mView;
    private PersonalInfoActivityContract.Model mModel;

    public PersonalInfoActivityPresenter(PersonalInfoActivityContract.Model model) {
        this.mModel = model;
    }

    @Override
    public void attachView(PersonalInfoActivityContract.View view) {
        mView = view;
    }

    @Override
    public void getPersonalInfoRequest() {
        mModel.getPersonalInfo(this);
    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void onSuccessRequestListener(JSONArray response) {
        if (mView != null) {
            try {
                UserModel userPersonalData = new UserModel(response.getJSONObject(0).getString("first_name"), response.getJSONObject(0).getString("last_name"));
                userPersonalData.setbDate(response.getJSONObject(0).getString("bdate"));
                userPersonalData.setAbout(response.getJSONObject(0).getString("about"));
                userPersonalData.setInterests(response.getJSONObject(0).getString("interests"));
                if (response.getJSONObject(0).isNull("photo_200"))
                    userPersonalData.setPhoto_200(response.getJSONObject(0).getString("photo_50"));
                else
                    userPersonalData.setPhoto_200(response.getJSONObject(0).getString("photo_200"));
                if (response.getJSONObject(0).isNull(("relation"))) {
                    userPersonalData.setRelation(0);
                } else
                    userPersonalData.setRelation(response.getJSONObject(0).getInt("relation"));
                userPersonalData.setRelation(response.getJSONObject(0).getInt("relation"));
                userPersonalData.setSex(response.getJSONObject(0).getInt("sex"));
                userPersonalData.setCountry(response.getJSONObject(0).getJSONObject("country").getString("title"));
                userPersonalData.setCity(response.getJSONObject(0).getJSONObject("city").getString("title"));
                long unixSeconds = Long.parseLong(response.getJSONObject(0).getJSONObject("last_seen").getString("time"));
                userPersonalData.setLastseen(getLastSeenDate(unixSeconds));
                mView.onSuccessResponseGetPersonalInfoRequest(userPersonalData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorRequestListener() {
        if (mView != null)
            mView.onErrorResponseGetPersonalInfoRequest();
    }

    private String getLastSeenDate(long timestamp) {
        Date date = new Date(timestamp * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }
}
