package app.line.good.androidtestapp.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import app.line.good.androidtestapp.R;
import app.line.good.androidtestapp.mvp.PersonalInfoActivityModel;
import app.line.good.androidtestapp.mvp.PersonalInfoActivityContract;
import app.line.good.androidtestapp.mvp.PersonalInfoActivityPresenter;
import app.line.good.androidtestapp.mvp.UserModel;
import app.line.good.androidtestapp.utils.Constants;

import android.app.Dialog;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.List;

import static app.line.good.androidtestapp.utils.Utils.getUserRelation;
import static app.line.good.androidtestapp.utils.Utils.getUserSex;

public class PersonalInfoActivity extends AppCompatActivity implements PersonalInfoActivityContract.View, NetworkChangeReceiver.ReceiverListener {

    private PersonalInfoActivityPresenter presenter;
    private NetworkChangeReceiver receiver;

    private TextView userLastSeenTv, userFirstNameTv, userLastNameTv, userBDateTv, userSexTv, userAboutTv,
            userInterestsTv, userCountryCityTv, userRelationTv;
    private ImageView userPhotoIv;
    private ProgressBar loadPhotoPb;

    private Dialog loadingDialog;

    private boolean isRestore = false;
    private String userFirstName, userLastName, userLastSeen, userCountryCity, userSex, userBDate,
            userRelation, userAbout, userInterests, userPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        initViews();
        initToolbar();

        receiver = new NetworkChangeReceiver();
        presenter = new PersonalInfoActivityPresenter(new PersonalInfoActivityModel());

        if (savedInstanceState != null) {
            isRestore = true;
            userLastSeen = savedInstanceState.getString(Constants.KEY_PERS_DATA_LASTSEEN);
            userFirstName = savedInstanceState.getString(Constants.KEY_FIRST_NAME);
            userLastName = savedInstanceState.getString(Constants.KEY_LAST_NAME);
            userBDate = savedInstanceState.getString(Constants.KEY_PERS_DATA_BDATE);
            userSex = savedInstanceState.getString(Constants.KEY_USER_SEX);
            userAbout = savedInstanceState.getString(Constants.KEY_PERS_DATA_ABOUT);
            userInterests = savedInstanceState.getString(Constants.KEY_PERS_DATA_INTERESTS);
            userCountryCity = savedInstanceState.getString(Constants.KEY_PERS_DATA_COUNTRY);
            userRelation = savedInstanceState.getString(Constants.KEY_RELATION);
            userPhotoUrl = savedInstanceState.getString(Constants.KEY_URL_PHOTO_200);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(this.receiver, intentFilter);
        setConnectivityListener(this);

        if (!isRestore)
            getUserData();
        else {
            fillTextViews();
            loadPhotoToIv(userPhotoUrl);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.destroy();
        this.unregisterReceiver(receiver);
        isRestore = false;
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_data_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.KEY_PERS_DATA_LASTSEEN, userLastSeen);
        outState.putString(Constants.KEY_FIRST_NAME, userFirstName);
        outState.putString(Constants.KEY_LAST_NAME, userLastName);
        outState.putString(Constants.KEY_PERS_DATA_BDATE, userBDate);
        outState.putString(Constants.KEY_USER_SEX, userSex);
        outState.putString(Constants.KEY_PERS_DATA_ABOUT, userAbout);
        outState.putString(Constants.KEY_PERS_DATA_INTERESTS, userInterests);
        outState.putString(Constants.KEY_PERS_DATA_COUNTRY, userCountryCity);
        outState.putString(Constants.KEY_RELATION, userRelation);
        outState.putString(Constants.KEY_URL_PHOTO_200, userPhotoUrl);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccessResponseGetPersonalInfoRequest(UserModel response) {
        loadingDialog.dismiss();

        userLastSeen = getString(R.string.pers_data_user_last_seen) + response.getLastseen();
        userFirstName = response.getFirstname();
        userLastName = response.getLastname();
        userBDate = response.getbDate();
        int intSex = response.getSex();
        userSex = getString(getUserSex(intSex));
        userAbout = response.getAbout();
        userInterests = response.getInterests();
        userCountryCity = response.getCountry() + "/" + response.getCity();
        userRelation = getString(getUserRelation((response.getRelation()), intSex));
        userPhotoUrl = response.getPhoto_200();

        loadPhotoPb.setVisibility(View.VISIBLE);
        fillTextViews();
        loadPhotoToIv(userPhotoUrl);
    }

    @Override
    public void onErrorResponseGetPersonalInfoRequest() {
        loadingDialog.dismiss();
        Toast.makeText(this, getString(R.string.error_toast_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
    }

    private void initViews() {
        userFirstNameTv = findViewById(R.id.tv_pers_firstname);
        userLastNameTv = findViewById(R.id.tv_pers_lastname);
        userSexTv = findViewById(R.id.tv_pers_sex);
        userBDateTv = findViewById(R.id.tv_pers_bdate);
        userPhotoIv = findViewById(R.id.iv_pers_photo_200);
        userRelationTv = findViewById(R.id.tv_pers_relation);
        userLastSeenTv = findViewById(R.id.tv_pers_last_seen);
        userAboutTv = findViewById(R.id.tv_pers_about);
        userInterestsTv = findViewById(R.id.tv_pers_interests);
        userCountryCityTv = findViewById(R.id.tv_pers_country_city);
        loadPhotoPb = findViewById(R.id.pb_load_pers_photo);
    }

    private void initToolbar() {
        Toolbar userDetailTb = findViewById(R.id.tb_pers_data);
        setSupportActionBar(userDetailTb);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userDetailTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setConnectivityListener(NetworkChangeReceiver.ReceiverListener listener) {
        receiver.receiverListener = listener;
    }

    private void showLoadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_progress_load);
        builder.setCancelable(false);
        loadingDialog = builder.create();
        loadingDialog.show();
    }

    public void refreshData(MenuItem item) {
        getUserData();
    }

    private void getUserData() {
        if (receiver.isOnline(this)) {
            showLoadDialog();
            presenter.getPersonalInfoRequest();
        } else {
            setEmptyDataForViews();
            Toast.makeText(PersonalInfoActivity.this, getString(R.string.no_connect_msg), Toast.LENGTH_SHORT).show();
        }
    }

    private void fillTextViews() {
        userLastSeenTv.setText(userLastSeen);
        userFirstNameTv.setText(userFirstName);
        userLastNameTv.setText(userLastName);
        userBDateTv.setText(userBDate);
        userSexTv.setText(userSex);
        userAboutTv.setText(userAbout);
        userInterestsTv.setText(userInterests);
        userCountryCityTv.setText(userCountryCity);
        userRelationTv.setText(userRelation);
    }

    private void loadPhotoToIv(String url) {
        Glide.with(this).load(url).apply(RequestOptions.circleCropTransform()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                loadPhotoPb.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                loadPhotoPb.setVisibility(View.GONE);
                return false;
            }
        }).error(Glide.with(this).load(ContextCompat.getDrawable(this, R.drawable.no_photo_icon))).into(userPhotoIv);
    }

    private void setEmptyDataForViews() {
        userFirstNameTv.setText("-");
        userLastNameTv.setText("-");
        userSexTv.setText("-");
        userBDateTv.setText("-");
        userRelationTv.setText("-");
        userLastSeenTv.setText("-");
        userAboutTv.setText("-");
        userInterestsTv.setText("-");
        userCountryCityTv.setText("-");
        Glide.with(this).load(ContextCompat.getDrawable(PersonalInfoActivity.this, R.drawable.no_photo_icon)).apply(RequestOptions.circleCropTransform()).into(userPhotoIv);
    }

}
