package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.experia.experia.R;

import models.Experience;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;
    public TextView tvSubtext;
    public ImageView starView;
    public ImageView ivExpand;
    public TextView tvDescription;

    public PostViewHolder(View itemView) {
        super(itemView);

        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvSubtext = (TextView) itemView.findViewById(R.id.tvSubtext);
//        starView = (ImageView) itemView.findViewById(R.id.star);
//        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        ivExpand = (ImageView) itemView.findViewById(R.id.ivExpand);
        tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        tvDescription.setVisibility(View.GONE);
    }

    public void bindToPost(Experience post, View.OnClickListener starClickListener) {
        tvTitle.setText(post.title);
        tvSubtext.setText(post.author);
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
//                tvDescription.setVisibility( tvDescription.isShown()
//                        ? View.GONE
//                        : View.VISIBLE );

            }
        });

//        starView.setOnClickListener(starClickListener);


    }
}
