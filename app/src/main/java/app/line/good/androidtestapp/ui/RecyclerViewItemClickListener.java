package app.line.good.androidtestapp.ui;

import android.view.View;

import org.json.JSONObject;

import app.line.good.androidtestapp.mvp.UserModel;

public interface RecyclerViewItemClickListener {
    void onItemClick(View view, UserModel response);
}
