package app.line.good.androidtestapp.mvp;

import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivityModel implements SearchActivityContract.Model {

    @Override
    public void searchUser(String query, int count, int offset, final OnFinishRequestListener onFinishRequestListener) {
        final VKRequest request = VKApi.users().search(VKParameters.from(VKApiConst.Q, query, VKApiConst.COUNT, count, VKApiConst.OFFSET, offset, VKApiConst.FIELDS, "first_name, last_name, sex, photo_200, photo_50, screen_name, relation"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                if (response != null) {
                    try {
                        onFinishRequestListener.onSuccessRequestListener(new JSONObject(response.responseString).getJSONObject("response"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(VKError error) {
                onFinishRequestListener.onErrorRequestListener();
                super.onError(error);
            }
        });
    }

}
