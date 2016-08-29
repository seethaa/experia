package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.experia.experia.R;

import models.Experience;

public class MapPostViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;
    public ImageView ivExperience;
    public TextView tvSpotsLeft;
    public Context mContext;

    public MapPostViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tvMapTitle);
        ivExperience = (ImageView) itemView.findViewById(R.id.ivMapExperienceImage);
        tvSpotsLeft = (TextView) itemView.findViewById(R.id.tvMapSpotsLeft);
    }

    public void bindToPost(Context applicationContext, Experience post) {
        mContext = applicationContext;

        tvTitle.setText(post.title);
        tvSpotsLeft.setText(Integer.toString(post.getSpotsLeft())+" spot(s) left");

        String img = post.imgURL;
        if (!TextUtils.isEmpty(post.imgURL)) {
            Glide.with(mContext).load(img)
                    .centerCrop()
                    .placeholder(R.drawable.placeholderimg)
                    .into(ivExperience);
        }
    }
}

