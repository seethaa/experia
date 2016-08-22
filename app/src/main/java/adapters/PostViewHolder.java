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

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;
    public TextView tvSubtext;
    public ImageView ivExperience;
    public ImageView ivExpand;
    public TextView tvDescription;
    public Context mContext;

    public PostViewHolder(View itemView) {
        super(itemView);


        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvSubtext = (TextView) itemView.findViewById(R.id.tvSubtext);
        ivExperience = (ImageView) itemView.findViewById(R.id.ivExperienceImage);
//        starView = (ImageView) itemView.findViewById(R.id.star);
//        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        ivExpand = (ImageView) itemView.findViewById(R.id.ivExpand);
        tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        tvDescription.setVisibility(View.GONE);

    }

    public void bindToPost(Context applicationContext, Experience post, View.OnClickListener starClickListener) {
        mContext = applicationContext;

        tvTitle.setText(post.title);
        tvSubtext.setText(post.tags);
//        numStarsView.setText(String.valueOf(post.starCount));
        tvDescription.setText(post.description);

        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tvDescription.isShown()){
                    tvDescription.setVisibility(View.GONE);
                    ivExpand.setImageResource(R.drawable.icon_chevron_up);

                }
                else{
                    tvDescription.setVisibility(View.VISIBLE);
                    ivExpand.setImageResource(R.drawable.icon_chevron_down);
                }

            }
        });

        String img = post.imgURL;
        if (!TextUtils.isEmpty(post.imgURL)) {
            Glide.with(mContext).load(img).centerCrop().placeholder(R.drawable.ic_bitmap_lg_crown)
                    .into(ivExperience);
//            .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(), 5, 5))


        }

//        starView.setOnClickListener(starClickListener);


    }
}
