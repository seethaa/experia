package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.experia.experia.R;
import com.experia.experia.activities.PostDetailActivity;

import java.util.ArrayList;
import java.util.List;

import models.Experience;

/**
 * Created by Sam on 8/30/16.
 */
public class MapFragmentAdapter extends
        RecyclerView.Adapter<MapFragmentAdapter.MapPostViewHolder> {
    private Context context;
    @Override
    public MapPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View experienceView = inflater.inflate(R.layout.map_item_experience, parent, false);

        // Return a new holder instance
        MapPostViewHolder viewHolder = new MapPostViewHolder(experienceView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MapPostViewHolder holder, int position) {
        // Get the data model based on position
        final Experience experience = mExperiences.get(position);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch PostDetailActivity
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, experience.key);
                context.startActivity(intent);
            }
        });

        // Set item views based on your views and data model
        holder.bindToPost(context, experience);
    }

    @Override
    public int getItemCount() {
        return mExperiences.size();
    }

    public static class MapPostViewHolder extends RecyclerView.ViewHolder {

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
            tvSpotsLeft.setText(Integer.toString(post.getSpotsLeft()) + " spot(s) left");

            String img = post.imgURL;
            if (!TextUtils.isEmpty(post.imgURL)) {
                Glide.with(mContext).load(img)
                        .centerCrop()
                        .placeholder(R.drawable.placeholderimg)
                        .into(ivExperience);
            }
        }
    }

    // Store a member variable for the experiences
    private List<Experience> mExperiences;
    // Store the context for easy access
    private Context mContext;

    // Pass in the experience array into the constructor
    public MapFragmentAdapter(Context context, ArrayList<Experience> experiences) {
        mExperiences = experiences;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
}
