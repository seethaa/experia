package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.experia.experia.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class BookmarksFragment extends Fragment {
    private static final String TAG = "DATA_TAG";
    private EditText mEditTextName;
    private EditText mEditTextDescription;
    private Button mButtonSend;
    private Firebase mRootRef;
    private DatabaseReference mDatabase;

    private static final String FIREBASE_URL = "https://experia-45c85.firebaseio.com/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_offer_new_experience, container, false);


        mButtonSend = (Button) v.findViewById(R.id.btnSubmit);
        mEditTextName = (EditText) v.findViewById(R.id.editText);
        mEditTextDescription = (EditText) v.findViewById(R.id.etDescription);

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Firebase mRefChild = mRef.child("name");
//                mRefChild.setValue("Sam");
                String name = mEditTextName.getText().toString();
                String desc = mEditTextDescription.getText().toString();

//                Experience exp1 = new Experience("test 1", "test 1 blah blah");
//                Experience exp2 = new Experience("test 2", "test 2 blah blah");

//                Firebase childRef = mRootRef.child(("Title"));
//                childRef.setValue(name);
//
//                Firebase childRef2 = mRootRef.child(("Description"));
//                childRef2.setValue(desc);
//                mRootRef.push().setValue(exp1);
//                mRootRef.push().setValue(exp2);


            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getContext());
        mRootRef = new Firebase(FIREBASE_URL);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Read from the database
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

    }



}
