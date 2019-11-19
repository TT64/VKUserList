package app.line.good.androidtestapp.mvp;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFriendActivityModel implements MyFriendsActivityContract.Model {
    @Override
    public void getFriends(int count, int offset, final OnFinishRequestListener onFinishRequestListener) {
        final VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.COUNT, count, VKApiConst.OFFSET, offset, "order", "hints", VKApiConst.FIELDS, "first_name, last_name, photo_50"));
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
                onFinishRequestListener.onErrorRequestListener();
            }
        });
    }
}
