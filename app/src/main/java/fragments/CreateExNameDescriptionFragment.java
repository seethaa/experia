package fragments;


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
import android.widget.ImageView;
import android.widget.Toast;

import com.experia.experia.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import models.Creation;
import util.CupboardDBHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


/**
 * Created by doc_dungeon on 8/27/16.
 */
public class CreateExNameDescriptionFragment extends Fragment {

    @BindView(R.id.fire_event)
    Button saveBtn;
    @BindView(R.id.query) Button checkBtn;
    @BindView(R.id.etName)
    EditText experienceName;
    @BindView(R.id.etDescription) EditText experienceDescription;
    @BindView(R.id.iv_icon)
    ImageView logoImageView;
    private Unbinder unbinder;
    static SQLiteDatabase db;

    private static final String ARG_SECTION_NUMBER = "section-icon";
    private static final String ARG_SECTION_COLOR = "section-color";

    public static CreateExNameDescriptionFragment newInstance(int color, int icon) {
        CreateExNameDescriptionFragment fragment = new CreateExNameDescriptionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, icon);
        args.putInt(ARG_SECTION_COLOR, color);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_name_description, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        rootView.setBackgroundColor(ContextCompat.getColor(getContext(), getArguments().getInt(ARG_SECTION_COLOR)));
        logoImageView.setImageResource(getArguments().getInt(ARG_SECTION_NUMBER));


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNameDescription(v);
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
        // Create a Bunny
        Creation ex = new Creation("nothing","nothing","nothing","nothing","nothing","nothing","nothing","nothing","nothing","nothing",0);
        long id = cupboard().withDatabase(db).put(ex);

        experienceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                saveBtn.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }


    public void setNameDescription(View view){


        //Sugar ORM
        //Creation creation = new Creation(exName, exDescription, exTimeDate, exStreet, exCity, exState);
        //creation.save();
    }

    public void checkDB(View view){
        //Sugar ORM
        //Creation last = Creation.last(Creation.class);
        Creation creation = cupboard().withDatabase(db).query(Creation.class).get();
        String entry = creation.title;
        Toast.makeText(getContext(),entry,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
