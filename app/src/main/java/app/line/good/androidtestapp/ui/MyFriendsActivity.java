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
import app.line.good.androidtestapp.mvp.MyFriendActivityModel;
import app.line.good.androidtestapp.mvp.MyFriendsActivityContract;
import app.line.good.androidtestapp.mvp.MyFriendsActivityPresenter;
import app.line.good.androidtestapp.mvp.UserModel;

import android.app.Dialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.List;

public class MyFriendsActivity extends AppCompatActivity implements MyFriendsActivityContract.View, NetworkChangeReceiver.ReceiverListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvFriendList;
    private SwipeRefreshLayout refreshLayout;
    private Dialog loadingDialog;
    private Toolbar tbFriends;
    private LinearLayoutManager mLayoutManager;
    private VkFriendsAdapter adapter;

    private MyFriendsActivityPresenter presenter;
    private NetworkChangeReceiver receiver;

    private int count = 20, offset = 0;
    private boolean loadingMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);

        presenter = new MyFriendsActivityPresenter(new MyFriendActivityModel());
        receiver = new NetworkChangeReceiver();
        initRecyclerView();

        refreshLayout = findViewById(R.id.srl_my_friends);
        refreshLayout.setOnRefreshListener(this);

        tbFriends = findViewById(R.id.tb_my_friends);
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
    public void onRefresh() {
        offset = 0;
        refreshLayout.setRefreshing(false);
        getFriendsData(offset);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            loadingMore = false;
        }
    }

    @Override
    public void onSuccessResponseGetFriendsRequest() {
        loadingDialog.dismiss();
        offset += count;
        setScrollListener();
    }

    @Override
    public void setDataToRecyclerView(List<UserModel> response) {
        if (!loadingMore) {
            adapter = new VkFriendsAdapter(Glide.with(MyFriendsActivity.this), response);
            rvFriendList.setAdapter(adapter);
        } else {
            adapter.updateData(response);
            loadingMore = false;
        }
        if (response.size() == 0)
            loadingMore = true;
    }

    @Override
    public void onErrorResponseGetFriendsRequest() {
        loadingDialog.dismiss();
        Toast.makeText(this, getString(R.string.error_toast_msg), Toast.LENGTH_SHORT).show();
    }

    private void initRecyclerView() {
        rvFriendList = findViewById(R.id.rv_my_friendslist);
        mLayoutManager = new LinearLayoutManager(this);
        rvFriendList.setLayoutManager(mLayoutManager);
        rvFriendList.setItemAnimator(new DefaultItemAnimator());
        rvFriendList.setHasFixedSize(true);
    }

    private void initToolbar() {
        setSupportActionBar(tbFriends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            presenter.getFriendsRequest(count, offset);
        } else
            Toast.makeText(MyFriendsActivity.this, getString(R.string.no_connect_msg), Toast.LENGTH_SHORT).show();
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
