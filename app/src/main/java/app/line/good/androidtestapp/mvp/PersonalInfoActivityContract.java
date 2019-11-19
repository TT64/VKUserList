package app.line.good.androidtestapp.mvp;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

public class PersonalInfoActivityContract {
    public interface View {

        void onSuccessResponseGetPersonalInfoRequest(UserModel response);

        void onErrorResponseGetPersonalInfoRequest();
    }

    public interface Presenter {
        void attachView(PersonalInfoActivityContract.View view);

        void getPersonalInfoRequest();

        void destroy();
    }

    public interface Model {
        interface OnFinishRequestListener {

            void onSuccessRequestListener(JSONArray response);

            void onErrorRequestListener();

        }

        void getPersonalInfo(OnFinishRequestListener onFinishRequestListener);

    }
}
