package adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.experia.experia.R;

import java.util.concurrent.TimeUnit;

import fragments.InfoDetailFragment;
import models.Experience;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;
    public TextView tvSubtext;
    public TextView tvTimeLeft;
    public TextView tvSpotsLeft;
    public ImageView ivExperience;
    public ImageView ivCategory;
    public TextView tvExpand;
    public TextView tvDescription;
    public Context mContext;
    public FloatingActionButton starView;
    public FloatingActionButton joinView;


    public PostViewHolder(View itemView) {
        super(itemView);


        tvTitle = (TextView) itemView.findViewById(R.id.etTitle);
        tvSubtext = (TextView) itemView.findViewById(R.id.tvSubtext);
        tvTimeLeft = (TextView) itemView.findViewById(R.id.tvTimeLeft);
        tvSpotsLeft = (TextView) itemView.findViewById(R.id.tvSpotsLeft);
        starView = (FloatingActionButton) itemView.findViewById(R.id.btnBookmark);
        joinView = (FloatingActionButton) itemView.findViewById(R.id.btnRSVP);
        ivExperience = (ImageView) itemView.findViewById(R.id.ivExperienceImage);
        ivCategory = (ImageView) itemView.findViewById(R.id.ivCategory);

//        starView = (ImageView) itemView.findViewById(R.id.star);
//        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        tvExpand = (TextView) itemView.findViewById(R.id.tvExpand);
        tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        tvDescription.setVisibility(View.GONE);

    }

    public void bindToPost(Context applicationContext, View itemView, Experience post, View.OnClickListener starClickListener, View.OnClickListener joinClickListener) {
        mContext = applicationContext;

        tvTitle.setText(post.title);

        String postTags = post.tags;
        if (postTags!=null) {
            postTags = postTags.trim();
            postTags = " " + postTags;
            postTags = postTags.replace(" ", " #");
        }

        tvSubtext.setText(postTags);
//        numStarsView.setText(String.valueOf(post.starCount));
        tvDescription.setText(post.description);

        String date = post.date;
        String time = post.time;
        final Countdown timer = new Countdown(InfoDetailFragment.getRelativeTimeAgo(date, time),1000); //first parameter number of milliseconds in future
        timer.start();



        if (post.getSpotsLeft()>1) {
            tvSpotsLeft.setText(post.getSpotsLeft() + " spots left");
        }
        if (post.getSpotsLeft()==1) {
            tvSpotsLeft.setText(post.getSpotsLeft() + " spot left");
        }

        if (post.getSpotsLeft()==0){
            tvSpotsLeft.setText("SOLD OUT!");
            tvSpotsLeft.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));

            joinView.setClickable(false);
        }



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
            Glide.with(mContext).load(img).centerCrop().placeholder(R.drawable.pattern)
                    .into(ivExperience);

//            Glide.with(itemView.getContext()).load(img).fitCenter().placeholder(R.drawable.pattern)
//                    .into(ivExperience);


        }
//        else{
////            Glide.with(mContext).load("http://i.imgur.com/ipVZhKi.png").centerCrop().placeholder(R.drawable.pattern_try)
////                    .into(ivExperience);
//
//            Glide.with(mContext).load("http://i.imgur.com/ipVZhKi.png").fitCenter().placeholder(R.drawable.pattern)
//                    .into(ivExperience);
//
//        }
        int catType = post.type;

        int resID = chooseIcon(catType);


//            Glide.with(mContext).load(resID).centerCrop().placeholder(R.drawable.icon_unknown)
//                    .into(ivCategory);
        ivCategory.setImageResource(resID);


        starView.setOnClickListener(starClickListener);

        joinView.setOnClickListener(joinClickListener);



    }

    public class Countdown extends CountDownTimer {

        public Countdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override public void onFinish() {

            tvTimeLeft.setText("Just missed!");
//            tvTimeLeft.setTextColor(ContextCompat.getColor(mContext, R.color.mp_brown));
        }

        @Override public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("in %02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(hms);
            tvTimeLeft.setText(hms);
//            if(millisUntilFinished <= 1000*60*899) {//less than 15 mins
//                tvTimeLeft.setTextColor(ContextCompat.getColor(mContext, R.color.red));
//            }
//            else{ //greater than or equal to 15 mins
//                tvTimeLeft.setTextColor(ContextCompat.getColor(mContext, R.color.black));
//            }
        }
    }

    public static int chooseIcon(int catType){
        int resID=R.drawable.icon_unknown;
        switch (catType) {
            case 1: //adventure
                resID = R.drawable.icon_map_adventure;
                break;
            case 2: //relax
                resID = R.drawable.icon_map_relax;
                break;
            case 3: //impact
                resID = R.drawable.icon_map_social;
                break;
            case 4: //learn
                resID = R.drawable.icon_map_learn;
                break;
            case 5: //fun
                resID = R.drawable.icon_map_fun;
                break;
            default: //unknown
                resID = R.drawable.icon_map_unknown;
                break;
        }
        return resID;
    }
}
