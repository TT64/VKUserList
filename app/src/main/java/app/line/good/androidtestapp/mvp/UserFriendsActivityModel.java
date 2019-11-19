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

public class UserFriendsActivityModel implements UserFriendsActivityContract.Model {

    @Override
    public void getUserFriends(int id, int count, int offset, final OnFinishRequestListener onFinishRequestListener) {
        if (id > 0) {
            final VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.USER_ID, id, VKApiConst.COUNT, 20, VKApiConst.OFFSET, offset, "order", "hints", VKApiConst.FIELDS, "first_name, last_name, photo_50"));
            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    if (response != null) {
                        try {
                            onFinishRequestListener.onSuccessRequestListener(new JSONObject(response.responseString).getJSONObject("response"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    super.onComplete(response);
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                    if (error != null) {
                        if (error.apiError.errorCode == 15) {
                            onFinishRequestListener.onErrorClosedProfileRequestListener();
                        } else {
                            onFinishRequestListener.onErrorRequestListener();
                        }
                    } else {
                        onFinishRequestListener.onErrorClosedProfileRequestListener();
                    }
                }
            });
        } else
            onFinishRequestListener.onErrorIncorrectIdRequestListener();
    }
}
