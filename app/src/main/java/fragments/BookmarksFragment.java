package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import models.User;


public class BookmarksFragment extends PostListFragment {
    private static final String TAG = "DATA_TAG";
    private EditText mEditTextName;
    private EditText mEditTextDescription;
    private Button mButtonSend;
    private Firebase mRootRef;
    private DatabaseReference mDatabase;

    private static final String FIREBASE_URL = "https://experia-45c85.firebaseio.com/";

    public static BookmarksFragment newInstance(User currUser) {
        BookmarksFragment fg = new BookmarksFragment();
        Bundle args = new Bundle();
        args.putParcelable("currUser", Parcels.wrap(currUser));
        fg.setArguments(args);
        return fg;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public BookmarksFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        System.out.println("DEBUGGY GETTING UID: " + getUid());
        // All my posts
//        return databaseReference.child("posts")
//                .child("stars").child(getUid()).orderByValue();

//        Query myTopPostsQuery = databaseReference.child("user-posts").child(getUid())
//                .orderByChild("starCount");

        Query bookmarksQuery = databaseReference.child("user-stars").child(getUid());
//                .orderByChild("stars");

        return bookmarksQuery;

    }


}
