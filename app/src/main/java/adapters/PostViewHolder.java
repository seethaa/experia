package adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.experia.experia.R;

import models.Experience;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;
    public TextView tvSubtext;
    public TextView tvSpotsLeft;
    public ImageView ivExperience;
    public ImageButton ivCategory;
    public TextView tvExpand;
    public TextView tvDescription;
    public Context mContext;
    public FloatingActionButton starView;
    public FloatingActionButton joinView;


    public PostViewHolder(View itemView) {
        super(itemView);


        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvSubtext = (TextView) itemView.findViewById(R.id.tvSubtext);
        tvSpotsLeft = (TextView) itemView.findViewById(R.id.tvSpotsLeft);
        starView = (FloatingActionButton) itemView.findViewById(R.id.btnBookmark);
        joinView = (FloatingActionButton) itemView.findViewById(R.id.btnRSVP);
        ivExperience = (ImageView) itemView.findViewById(R.id.ivExperienceImage);
        ivCategory = (ImageButton) itemView.findViewById(R.id.ivCategory);

//        starView = (ImageView) itemView.findViewById(R.id.star);
//        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        tvExpand = (TextView) itemView.findViewById(R.id.tvExpand);
        tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        tvDescription.setVisibility(View.GONE);

    }

    public void bindToPost(Context applicationContext, Experience post, View.OnClickListener starClickListener, View.OnClickListener joinClickListener) {
        mContext = applicationContext;

        tvTitle.setText(post.title);
        tvSubtext.setText(post.tags);
//        numStarsView.setText(String.valueOf(post.starCount));
        tvDescription.setText(post.description);
        tvSpotsLeft.setText(post.getSpotsLeft() + " spots left");

        tvExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tvDescription.isShown()){
                    tvDescription.setVisibility(View.GONE);
//                    tvExpand.setImageResource(R.drawable.icon_chevron_up);
                    tvExpand.setCompoundDrawablesWithIntrinsicBounds(
                            0, //left
                            0, //top
                            R.drawable.icon_chevron_up, //right
                            0);//bottom

                }
                else{
                    tvDescription.setVisibility(View.VISIBLE);
//                    ivExpand.setImageResource(R.drawable.icon_chevron_down);
                    tvExpand.setCompoundDrawablesWithIntrinsicBounds(
                            0, //left
                            0, //top
                            R.drawable.icon_chevron_down, //right
                            0);//bottom
                }

            }
        });

        String img = post.imgURL;
        if (!TextUtils.isEmpty(post.imgURL)) {
            Glide.with(mContext).load(img).centerCrop().placeholder(R.drawable.placeholderimg)
                    .into(ivExperience);
//            .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(), 5, 5))


        }

        int catType = post.type;
        int resID=R.drawable.icon_unknown;
        switch (catType) {
            case 1: //athlete
                resID = R.drawable.icon_athlete_a;
                break;
            case 2: //relax
                resID = R.drawable.icon_relax;
                break;
            case 3: //impact
                resID = R.drawable.icon_impact;
                break;
            case 4: //music
                resID = R.drawable.icon_music;
                break;
            case 5: //art
                resID = R.drawable.icon_arts;
                break;
            case 6: //outdoors
                resID = R.drawable.icon_outdoors;
                break;
            case 7: //skill/learn
                resID = R.drawable.icon_learn;
                break;
            default: //unknown
                resID = R.drawable.icon_unknown;
                break;
        }
//            Glide.with(mContext).load(resID).centerCrop().placeholder(R.drawable.icon_unknown)
//                    .into(ivCategory);
        ivCategory.setImageResource(resID);


        starView.setOnClickListener(starClickListener);
        joinView.setOnClickListener(joinClickListener);


    }
}
