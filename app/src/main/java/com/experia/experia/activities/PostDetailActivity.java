package com.experia.experia.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.experia.experia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fragments.DetailFragment;
import models.Experience;

public class PostDetailActivity extends BaseActivity {

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    public DatabaseReference mPostReference;
    protected DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;

    private Experience experience;
//    private CommentAdapter mAdapter;

    private TextView mTitleView;

    private TextView mSpotsAvailable;
    private ImageView mImageViewExperience;



    private EditText mCommentField;
    private Button mCommentButton;
    private RecyclerView mCommentsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_detail_with_tabs);

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("post-comments").child(mPostKey);

        // Initialize Views
//        mAuthorView = (TextView) findViewById(R.id.tvHostName);
        mTitleView = (TextView) findViewById(R.id.tvExperienceTitle);
//        mBodyView = (TextView) findViewById(R.id.tvDescription);
//        mAddress = (TextView) findViewById(R.id.tvLocationAddress);
//        mDate = (TextView) findViewById(R.id.tvDate);
//        mSpotsAvailable = (TextView) findViewById(R.id.tvSpotsLeft);
        mImageViewExperience = (ImageView) findViewById(R.id.ivExperienceImage);
//
//
//        mCommentField = (EditText) findViewById(R.id.field_comment_text);
//        mCommentButton = (Button) findViewById(R.id.button_post_comment);
//        mCommentsRecycler = (RecyclerView) findViewById(R.id.recycler_comments);
//
//        mCommentButton.setOnClickListener(this);
//        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));



//        setupFragmentTabs();

    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                 experience = dataSnapshot.getValue(Experience.class);


                createDetailFragment(experience);
                // [START_EXCLUDE]
//                mAuthorView.setText(post.author);
                mTitleView.setText(experience.title);
//                mBodyView.setText(post.description);
//                mAuthorView.setText(post.author);
//                mDate.setText(post.date);
//                mSpotsAvailable.setText(experience.getSpotsLeft() + " spots left");
//                mAddress.setText(post.address);


                String img = experience.imgURL;
                if (!TextUtils.isEmpty(experience.imgURL)) {
                    Glide.with(getApplicationContext()).load(img).centerCrop().placeholder(R.drawable.ic_bitmap_lg_crown)
                            .into(mImageViewExperience);
//            .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(), 5, 5))


                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

        // Listen for comments
//        mAdapter = new CommentAdapter(this, mCommentsReference);
//        mCommentsRecycler.setAdapter(mAdapter);

    }

    private void createDetailFragment(Experience experience) {
        DetailFragment fragmentUserTimeline = DetailFragment.newInstance(experience, mPostKey);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, fragmentUserTimeline);
        ft.commit();
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
//
//        // Clean up comments listener
//        mAdapter.cleanupListener();
    }

//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.button_post_comment) {
//            postComment();
//        }
//    }

//    private void postComment() {
//        final String uid = getUid();
//        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get user information
//                        User user = dataSnapshot.getValue(User.class);
//                        String authorName = user.username;
//
//                        // Create new comment object
//                        String commentText = mCommentField.getText().toString();
//                        Comment comment = new Comment(uid, authorName, commentText);
//
//                        // Push the comment, it will appear in the list
//                        mCommentsReference.push().setValue(comment);
//
//                        // Clear the field
//                        mCommentField.setText(null);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//    }
//
//    private static class CommentViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView authorView;
//        public TextView bodyView;
//
//        public CommentViewHolder(View itemView) {
//            super(itemView);
//
//            authorView = (TextView) itemView.findViewById(R.id.comment_author);
//            bodyView = (TextView) itemView.findViewById(R.id.comment_body);
//        }
//    }
//
//    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
//
//        private Context mContext;
//        private DatabaseReference mDatabaseReference;
//        private ChildEventListener mChildEventListener;
//
//        private List<String> mCommentIds = new ArrayList<>();
//        private List<Comment> mComments = new ArrayList<>();
//
//        public CommentAdapter(final Context context, DatabaseReference ref) {
//            mContext = context;
//            mDatabaseReference = ref;
//
//            // Create child event listener
//            // [START child_event_listener_recycler]
//            ChildEventListener childEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
//
//                    // A new comment has been added, add it to the displayed list
//                    Comment comment = dataSnapshot.getValue(Comment.class);
//
//                    // [START_EXCLUDE]
//                    // Update RecyclerView
//                    mCommentIds.add(dataSnapshot.getKey());
//                    mComments.add(comment);
//                    notifyItemInserted(mComments.size() - 1);
//                    // [END_EXCLUDE]
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
//
//                    // A comment has changed, use the key to determine if we are displaying this
//                    // comment and if so displayed the changed comment.
//                    Comment newComment = dataSnapshot.getValue(Comment.class);
//                    String commentKey = dataSnapshot.getKey();
//
//                    // [START_EXCLUDE]
//                    int commentIndex = mCommentIds.indexOf(commentKey);
//                    if (commentIndex > -1) {
//                        // Replace with the new data
//                        mComments.set(commentIndex, newComment);
//
//                        // Update the RecyclerView
//                        notifyItemChanged(commentIndex);
//                    } else {
//                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
//                    }
//                    // [END_EXCLUDE]
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
//
//                    // A comment has changed, use the key to determine if we are displaying this
//                    // comment and if so remove it.
//                    String commentKey = dataSnapshot.getKey();
//
//                    // [START_EXCLUDE]
//                    int commentIndex = mCommentIds.indexOf(commentKey);
//                    if (commentIndex > -1) {
//                        // Remove data from the list
//                        mCommentIds.remove(commentIndex);
//                        mComments.remove(commentIndex);
//
//                        // Update the RecyclerView
//                        notifyItemRemoved(commentIndex);
//                    } else {
//                        Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
//                    }
//                    // [END_EXCLUDE]
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
//
//                    // A comment has changed position, use the key to determine if we are
//                    // displaying this comment and if so move it.
//                    Comment movedComment = dataSnapshot.getValue(Comment.class);
//                    String commentKey = dataSnapshot.getKey();
//
//                    // ...
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
//                    Toast.makeText(mContext, "Failed to load comments.",
//                            Toast.LENGTH_SHORT).show();
//                }
//            };
//            ref.addChildEventListener(childEventListener);
//            // [END child_event_listener_recycler]
//
//            // Store reference to listener so it can be removed on app stop
//            mChildEventListener = childEventListener;
//        }
//
//        @Override
//        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(mContext);
//            View view = inflater.inflate(R.layout.item_comment, parent, false);
//            return new CommentViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(CommentViewHolder holder, int position) {
//            Comment comment = mComments.get(position);
//            holder.authorView.setText(comment.author);
//            holder.bodyView.setText(comment.text);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mComments.size();
//        }
//
//        public void cleanupListener() {
//            if (mChildEventListener != null) {
//                mDatabaseReference.removeEventListener(mChildEventListener);
//            }
//        }
//
//    }

//
//}public class DetailsPagerAdapter extends SmartFragmentStatePagerAdapter {
//        private String[] tabTitles = {"Info", "Reviews", "Going"};
        //        private Map<Integer, DetailFragment> map = new HashMap<>();
//        private User account;
//
//        // Adapter gets the manager insert or remove fragment from activity
//        public DetailsPagerAdapter(FragmentManager fm, User account) {
//            super(fm);
//            this.account = account;
//        }
//
//        // Decide fragment by tab position
//        @Override
//        public Fragment getItem(int position) {
//            DetailFragment fg = null;
//            if (position == 0) {
//                fg = InfoDetailFragment.newInstance(account);
//            } else if (position == 1) {
//                fg = InfoDetailFragment.newInstance(account);
//            } else {
//                fg = InfoDetailFragment.newInstance(account);
//            }
//            map.put(position, fg);
//            return fg;
//        }
//
//        // Decide tab name by tab position
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return tabTitles[position];
//        }
//
//        // get total count of fragments
//        @Override
//        public int getCount() {
//            return tabTitles.length;
//        }
//
//        // Get created fragment by position
//        public DetailFragment getCurrentFragment(int position) {
//            return map.get(position);
//        }
//    }
}
