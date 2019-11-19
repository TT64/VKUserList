package app.line.good.androidtestapp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import app.line.good.androidtestapp.mvp.UserModel;
import app.line.good.androidtestapp.utils.Constants;
import app.line.good.androidtestapp.R;
import app.line.good.androidtestapp.mvp.SearchActivityContract;
import app.line.good.androidtestapp.mvp.SearchActivityModel;
import app.line.good.androidtestapp.mvp.SearchActivityPresenter;

public class SearchActivity extends AppCompatActivity implements SearchActivityContract.View, NetworkChangeReceiver.ReceiverListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvUserList;
    private Dialog loadingDialog;
    private LinearLayoutManager mLayoutManager;
    private SearchView searchView;
    private SwipeRefreshLayout refreshLayout;

    private VkUsersAdapter adapter;
    private SearchActivityPresenter presenter;
    private NetworkChangeReceiver receiver;

    private SharedPreferences prefs;

    private boolean isGrantPerm = false, loadingMore = false;
    private int offset = 0, count = 20;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        receiver = new NetworkChangeReceiver();
        presenter = new SearchActivityPresenter(new SearchActivityModel());

        refreshLayout = findViewById(R.id.srl_users);
        refreshLayout.setOnRefreshListener(this);

        Toolbar searchTb = findViewById(R.id.tb_search);
        setSupportActionBar(searchTb);
        getSupportActionBar().setTitle("");

        initRecyclerView();

        prefs = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        isGrantPerm = prefs.getBoolean(Constants.KEY_IS_GRANT_PERM, false);
        if (!isGrantPerm) {
            VKSdk.login(SearchActivity.this, VKScope.WALL);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(receiver);
        presenter.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search_users).getActionView();
        searchView.setQueryHint(getString(R.string.search_view_hint));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        if (isGrantPerm)
            changeFocusOnSearchView(false);
        else
            changeFocusOnSearchView(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                offset = 0;
                if (!isGrantPerm)
                    Toast.makeText(SearchActivity.this, getString(R.string.perm_deny_msg), Toast.LENGTH_SHORT).show();
                else {
                    query = q;
                    if (receiver.isOnline(SearchActivity.this)) {
                        presenter.searchUserRequest(query, count, offset);
                        showLoadDialog();
                    } else
                        Toast.makeText(SearchActivity.this, getString(R.string.no_connect_msg), Toast.LENGTH_SHORT).show();
                }
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    rvUserList.setVisibility(View.GONE);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            rvUserList.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {
        offset = 0;
        if (receiver.isOnline(SearchActivity.this)) {
            showLoadDialog();
            presenter.searchUserRequest(query, count, offset);
            refreshLayout.setRefreshing(false);
        } else
            Toast.makeText(SearchActivity.this, getString(R.string.no_connect_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                findViewById(R.id.btn_login_vk).setVisibility(View.GONE);
                prefs.edit().putBoolean(Constants.KEY_IS_GRANT_PERM, true).apply();
                isGrantPerm = true;
                changeFocusOnSearchView(false);
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(SearchActivity.this, getString(R.string.perm_deny_msg), Toast.LENGTH_SHORT).show();
                isGrantPerm = false;
                findViewById(R.id.btn_login_vk).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_login_vk).bringToFront();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSuccessResponseSearchUserRequest() {
        loadingDialog.dismiss();
        offset += count;
        setScrollListener();
    }

    @Override
    public void setDataToRecyclerView(List<UserModel> response) {

        rvUserList.setVisibility(View.VISIBLE);

        if (!loadingMore) {
            adapter = new VkUsersAdapter(Glide.with(SearchActivity.this), response);
            rvUserList.setAdapter(adapter);
            adapter.setClickListener(new RecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, UserModel response) {
                    startUserDetailActivity(response);
                }
            });
        } else {
            adapter.updateData(response);
            loadingMore = false;
        }

        if (response.size() == 0)
            loadingMore = true;

    }

    @Override
    public void onErrorResponseSearchUserRequest() {
        loadingDialog.dismiss();
        Toast.makeText(this, getString(R.string.error_toast_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            loadingMore = false;
        }
    }

    private void initRecyclerView() {
        rvUserList = findViewById(R.id.rv_userlist);
        mLayoutManager = new LinearLayoutManager(this);
        rvUserList.setLayoutManager(mLayoutManager);
        rvUserList.setItemAnimator(new DefaultItemAnimator());
        rvUserList.setHasFixedSize(true);
    }

    private void changeFocusOnSearchView(boolean isClearFocus) {
        searchView.setIconified(isClearFocus);
    }

    private void startUserDetailActivity(UserModel response) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(Constants.KEY_USER_ID, response.getId());
        intent.putExtra(Constants.KEY_FIRST_NAME, response.getFirstname());
        intent.putExtra(Constants.KEY_LAST_NAME, response.getLastname());
        intent.putExtra(Constants.KEY_SCREEN_NAME, response.getScreenname());
        intent.putExtra(Constants.KEY_USER_SEX, response.getSex());
        intent.putExtra(Constants.KEY_URL_PHOTO_200, response.getPhoto_200());
        intent.putExtra(Constants.KEY_RELATION, response.getRelation());
        startActivity(intent);
    }

    private void showLoadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_progress_load);
        builder.setCancelable(false);
        loadingDialog = builder.create();
        loadingDialog.show();
    }

    private void setScrollListener() {
        rvUserList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisible, totalItem, visibleItems;
                visibleItems = mLayoutManager.getChildCount();
                totalItem = mLayoutManager.getItemCount();
                lastVisible = mLayoutManager.findFirstVisibleItemPosition();
                if (visibleItems + lastVisible >= totalItem && !loadingMore) {
                    if (receiver.isOnline(SearchActivity.this)) {
                        presenter.searchUserRequest(query, count, offset);
                        showLoadDialog();
                    } else {
                        Toast.makeText(SearchActivity.this, getString(R.string.no_connect_msg), Toast.LENGTH_SHORT).show();
                    }
                    loadingMore = true;
                }
            }
        });
    }

    public void setConnectivityListener(NetworkChangeReceiver.ReceiverListener listener) {
        receiver.receiverListener = listener;
    }

    public void authVk(View view) {
        VKSdk.login(SearchActivity.this, VKScope.WALL);
    }

    public void getFriends(MenuItem item) {
        if (!isGrantPerm)
            Toast.makeText(SearchActivity.this, getString(R.string.perm_deny_msg), Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(this, MyFriendsActivity.class);
            startActivity(intent);
        }
    }

    public void getPersonalInfo(MenuItem item) {
        if (!isGrantPerm)
            Toast.makeText(SearchActivity.this, getString(R.string.perm_deny_msg), Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(this, PersonalInfoActivity.class);
            startActivity(intent);
        }
    }

}
