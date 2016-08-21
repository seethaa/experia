//package adapters;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.experia.experia.R;
//
//import java.util.ArrayList;
//
//import models.ExperienceTestArray;
//
///**
// * Created by doc_dungeon on 8/20/16.
// */
//
//public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.UserViewHolder> {
//    ArrayList<ExperienceTestArray> users;
//
//    public ExperienceAdapter(ArrayList<ExperienceTestArray> users){
//        this.users = users;
//    }
//
//    @Override
//    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        //inflate the custom layout
//        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_experience, parent, false);
//
//        // Return a new holder instance
//
//        return new ExperienceAdapter.UserViewHolder(rootView);
//    }
//
//    @Override
//    public void onBindViewHolder(UserViewHolder holder, int position) {
//        //get the data model based on position
//        ExperienceTestArray user = users.get(position);
//        //Set item views based on the data model
//        holder.tvExperienceName.setText(user.name);
//        holder.tvHometown.setText(user.hometown);
//    }
//
//    @Override
//    public int getItemCount() {
//        return users.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//        private TextView tvExperienceName;
//        private TextView tvHometown;
//
//        public UserViewHolder(View rootView) {
//            super(rootView);
//
//            tvExperienceName = (TextView) rootView.findViewById(R.id.tvUserName);
//            tvHometown = (TextView) rootView.findViewById(R.id.tvHometown);
//        }
//    }
//
//}
