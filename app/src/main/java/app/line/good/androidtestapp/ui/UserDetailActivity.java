package app.line.good.androidtestapp.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import app.line.good.androidtestapp.utils.Constants;
import app.line.good.androidtestapp.R;
import app.line.good.androidtestapp.utils.Utils;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailActivity extends AppCompatActivity {

    private TextView userFirstNameTv, userLastNameTv, userSexTv, userScreennameTv, userRelationTv;
    private ImageView userPhotoIv;
    private ProgressBar loadPhotoPb;
    private Toolbar userDetailTb;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initViews();
        initToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        String userFirstName = intent.getStringExtra(Constants.KEY_FIRST_NAME);
        String userLastName = intent.getStringExtra(Constants.KEY_LAST_NAME);
        userFirstNameTv.setText(userFirstName);
        userLastNameTv.setText(userLastName);
        getSupportActionBar().setTitle(userFirstName + " " + userLastName);
        int userSex = intent.getIntExtra(Constants.KEY_USER_SEX, 0);
        userSexTv.setText(getString(Utils.getUserSex(userSex)));
        userScreennameTv.setText(intent.getStringExtra(Constants.KEY_SCREEN_NAME));
        userRelationTv.setText(Utils.getUserRelation(intent.getIntExtra(Constants.KEY_RELATION, 0), userSex));
        loadPhotoPb.setVisibility(View.VISIBLE);
        Glide.with(this).load(intent.getStringExtra(Constants.KEY_URL_PHOTO_200)).apply(RequestOptions.circleCropTransform()).listener(new RequestListener<Drawable>() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_friends_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initViews() {
        userFirstNameTv = findViewById(R.id.tv_user_firstname);
        userLastNameTv = findViewById(R.id.tv_user_lastname);
        userSexTv = findViewById(R.id.tv_user_sex);
        userScreennameTv = findViewById(R.id.tv_user_scrname);
        userPhotoIv = findViewById(R.id.iv_user_photo_200);
        userRelationTv = findViewById(R.id.tv_user_relation);
        loadPhotoPb = findViewById(R.id.pb_load_photo);
        userDetailTb = findViewById(R.id.tb_user_detail);
    }

    private void initToolbar() {
        setSupportActionBar(userDetailTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userDetailTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getUserFriends(MenuItem item) {
        Intent intent = new Intent(UserDetailActivity.this, UserFriendsActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
