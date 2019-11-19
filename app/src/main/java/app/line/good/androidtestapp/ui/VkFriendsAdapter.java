package app.line.good.androidtestapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.line.good.androidtestapp.R;
import app.line.good.androidtestapp.mvp.UserModel;

public class VkFriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int VIEW_FILL_DATA = 1;
    private int VIEW_EMPTY_DATA = 2;

    private List<UserModel> userValues;
    private RequestManager mGlide;

    VkFriendsAdapter(RequestManager glide, List<UserModel> values) {
        this.userValues = values;
        this.mGlide = glide;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_FILL_DATA)
            return new VkFriendsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false));
        else
            return new VkFriendsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false));

    }

    @Override
    public int getItemViewType(int position) {
        if (userValues.size() == 0)
            return VIEW_EMPTY_DATA;
        else return VIEW_FILL_DATA;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (userValues.size() > 0) {
            UserModel currentUser = userValues.get(position);
            String urlUserPhoto = currentUser.getPhoto_50(),
                    userName = currentUser.getFirstname() + " " + currentUser.getLastname();
            mGlide.load(urlUserPhoto).apply(RequestOptions.circleCropTransform()).into(((ViewHolder) holder).photoIv);
            ((ViewHolder) holder).friendNameTv.setText(userName);
        }
    }

    @Override
    public int getItemCount() {
        if (userValues.size() == 0)
            return 1;
        else
            return userValues.size();
    }

    void updateData(List<UserModel> values) {
        userValues.addAll(values);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView photoIv;
        private TextView friendNameTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            photoIv = itemView.findViewById(R.id.iv_friend_photo_50);
            friendNameTv = itemView.findViewById(R.id.tv_friend_firstname);

        }

    }
}
