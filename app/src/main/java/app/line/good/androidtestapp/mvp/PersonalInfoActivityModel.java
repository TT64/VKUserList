package app.line.good.androidtestapp.mvp;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;

public class PersonalInfoActivityModel implements PersonalInfoActivityContract.Model {
    @Override
    public void getPersonalInfo(final OnFinishRequestListener onFinishRequestListener) {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "sex, bdate, about, interests, relation, country, city, last_seen, photo_200, photo_50"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                if (response != null)
                    try {
                        onFinishRequestListener.onSuccessRequestListener(response.json.getJSONArray("response"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                onFinishRequestListener.onErrorRequestListener();
            }
        });
    }
}
