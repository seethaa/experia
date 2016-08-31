package fragments;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.experia.experia.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import models.Creation;
import util.CupboardDBHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class CreateExTotalTagsTypeFragment extends Fragment {

    @BindView(R.id.fire_event) Button saveBtn;
    @BindView(R.id.query) Button checkBtn;
    @BindView(R.id.etTotal) EditText etTotal;
    @BindView(R.id.etTags) EditText etTags;
    @BindView(R.id.etType) EditText etType;

    //@BindView(R.id.iv_icon) ImageView logoImageView;
    private Unbinder unbinder;
    static SQLiteDatabase db;

    private static final String ARG_SECTION_NUMBER = "section-icon";
    private static final String ARG_SECTION_COLOR = "section-color";

    int mTotal;
    String mTags;
    int mType;

    private OnTotaltagsTypeCompleteListener listener;

    // Define the events that the fragment will use to communicate
    public interface OnTotaltagsTypeCompleteListener {
        public void onTotaltagsTypeCompleted(int total, String tags, int type);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTotaltagsTypeCompleteListener) {
            listener = (OnTotaltagsTypeCompleteListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnNameDescriptionPhotoCompleteListener");
        }
    }

    // Now we can fire the event when the user selects something in the fragment
    public void onSaveTotalTagsTypeClick(View v) {
        mTotal = Integer.parseInt(etTotal.getText().toString());
        mTags = etTags.getText().toString();
        mType = Integer.parseInt(etType.getText().toString());

        System.out.println("DEBUGGY Exp 3 old: " + mTotal + ", " + mTags);
        listener.onTotaltagsTypeCompleted(mTotal, mTags, mType);
    }

    public static CreateExTotalTagsTypeFragment newInstance(int color, int icon) {
        CreateExTotalTagsTypeFragment fragment = new CreateExTotalTagsTypeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, icon);
        args.putInt(ARG_SECTION_COLOR, color);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_total_tags_type, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        rootView.setBackgroundColor(ContextCompat.getColor(getContext(), getArguments().getInt(ARG_SECTION_COLOR)));
        //logoImageView.setImageResource(getArguments().getInt(ARG_SECTION_NUMBER));


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveTotalTagsTypeClick(v);
//                saveNameDescription(v);
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDB(v);
            }
        });

        CupboardDBHelper dbHelper = new CupboardDBHelper(getContext());
        db = dbHelper.getWritableDatabase();
        // Initialize DB record
        String emptyStr = "";
        Creation ex = new Creation(emptyStr,emptyStr,emptyStr,emptyStr,emptyStr,emptyStr,emptyStr,
                emptyStr,emptyStr,emptyStr,0);
        long id = cupboard().withDatabase(db).put(ex);

        etTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etTags.setVisibility(View.VISIBLE);
            }
        });


        etTags.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                etType.setVisibility(View.VISIBLE);

                saveBtn.setVisibility(View.VISIBLE);
            }
        });

        etType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveBtn.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }

    public void checkDB(View view){
        //Cupboard ORM
        Creation creation = cupboard().withDatabase(db).query(Creation.class).get();
        String entry = creation.title +"-"+ creation.description;
        Toast.makeText(getContext(),entry,Toast.LENGTH_SHORT).show();
    }

    public void saveNameDescription(View view) {

        System.out.println("DEBUGGY Exp 1 old: " + mTotal + ", " + mTags);

        //set values obj
        ContentValues values = new ContentValues(1);
        values.put("title", mTotal);
        values.put("description", mTags);

        // update first record
        cupboard().withDatabase(db).update(Creation.class, values, "_id = ?", "1");
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
