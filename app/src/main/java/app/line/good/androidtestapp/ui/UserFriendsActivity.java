package app.line.good.androidtestapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import app.line.good.androidtestapp.R;
import app.line.good.androidtestapp.mvp.UserFriendsActivityContract;
import app.line.good.androidtestapp.mvp.UserFriendsActivityModel;
import app.line.good.androidtestapp.mvp.UserFriendsActivityPresenter;
import app.line.good.androidtestapp.mvp.UserModel;
import app.line.good.androidtestapp.utils.Constants;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.List;

public class UserFriendsActivity extends AppCompatActivity implements UserFriendsActivityContract.View, NetworkChangeReceiver.ReceiverListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvFriendList;
    private SwipeRefreshLayout refreshLayout;
    private Dialog loadingDialog;
    private Toolbar tbFriends;
    private LinearLayoutManager mLayoutManager;
    private VkFriendsAdapter adapter;

    private UserFriendsActivityPresenter presenter;
    private NetworkChangeReceiver receiver;

    private int count = 20, offset = 0;
    private int id;
    private boolean loadingMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friends);

        Intent intent = getIntent();
        id = intent.getIntExtra(Constants.KEY_USER_ID, 0);

        presenter = new UserFriendsActivityPresenter(new UserFriendsActivityModel());
        receiver = new NetworkChangeReceiver();
        initRecyclerView();

        refreshLayout = findViewById(R.id.srl_user_friends);
        refreshLayout.setOnRefreshListener(this);

        tbFriends = findViewById(R.id.tb_user_friends);
        initToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(this.receiver, intentFilter);
        setConnectivityListener(this);

        getFriendsData(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(receiver);
        presenter.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_data_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSuccessResponseGetUserFriendsRequest() {
        loadingDialog.dismiss();
        offset += count;
        setScrollListener();
    }

    @Override
    public void setDataToRecyclerView(List<UserModel> response) {
        if (!loadingMore) {
            adapter = new VkFriendsAdapter(Glide.with(UserFriendsActivity.this), response);
            rvFriendList.setAdapter(adapter);
        } else {
            adapter.updateData(response);
            loadingMore = false;
        }
        if (response.size() == 0)
            loadingMore = true;
    }

    @Override
    public void onErrorResponseGetUserFriendsRequest() {
        loadingDialog.dismiss();
        Toast.makeText(this, getString(R.string.error_toast_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorClosedProfileResponseGetUserFriendsRequest() {
        loadingDialog.dismiss();
        Toast.makeText(this, getString(R.string.error_private_profile_toast_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorIncorrectIdResponseGetUserFriendsRequest() {
        loadingDialog.dismiss();
        Toast.makeText(this, getString(R.string.error_incorrect_id_toast_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        offset = 0;
        refreshLayout.setRefreshing(false);
        getFriendsData(0);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            loadingMore = false;
        }
    }

    private void initRecyclerView() {
        rvFriendList = findViewById(R.id.rv_user_friendslist);
        mLayoutManager = new LinearLayoutManager(this);
        rvFriendList.setLayoutManager(mLayoutManager);
        rvFriendList.setItemAnimator(new DefaultItemAnimator());
        rvFriendList.setHasFixedSize(true);
    }

    private void initToolbar() {
        setSupportActionBar(tbFriends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        tbFriends.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setScrollListener() {
        rvFriendList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisible, totalItem, visibleItems;
                visibleItems = mLayoutManager.getChildCount();
                totalItem = mLayoutManager.getItemCount();
                lastVisible = mLayoutManager.findFirstVisibleItemPosition();
                if (visibleItems + lastVisible >= totalItem && !loadingMore) {
                    getFriendsData(offset);
                    loadingMore = true;
                }
            }
        });
    }

    private void getFriendsData(int offset) {
        if (receiver.isOnline(this)) {
            showLoadDialog();
            loadingMore = false;
            presenter.getUserFriendsRequest(id, count, offset);
        } else
            Toast.makeText(UserFriendsActivity.this, getString(R.string.no_connect_msg), Toast.LENGTH_SHORT).show();
    }

    private void showLoadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_progress_load);
        builder.setCancelable(false);
        loadingDialog = builder.create();
        loadingDialog.show();
    }

    public void refreshData(MenuItem item) {
        if (receiver.isOnline(this)) {
            offset = 0;
        }
        getFriendsData(offset);
    }

    public void setConnectivityListener(NetworkChangeReceiver.ReceiverListener listener) {
        receiver.receiverListener = listener;
    }
}
