package fragments;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import models.User;

public class RecentPostsFragment extends PostListFragment {

    public RecentPostsFragment() {}


    public static RecentPostsFragment newInstance(User currUser) {
        RecentPostsFragment fg = new RecentPostsFragment();
        Bundle args = new Bundle();
        args.putParcelable("currUser", Parcels.wrap(currUser));
        fg.setArguments(args);
        return fg;
    }
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("posts")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }
}
