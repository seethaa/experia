package fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import models.CreateNewExperience;

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

        deleteRecords(rootView);

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

        return rootView;
    }

    public void deleteRecords(View view){
        //empty records
        //CreateNewExperience.deleteAll(CreateNewExperience.class);
    }

    public void setNameDescription(View view){

        String exName  = experienceName.getText().toString();
        String exDescription = experienceDescription.getText().toString();
        String exTimeDate = "";
        String exStreet = "";
        String exCity = "";
        String exState = "";

        //create record
        CreateNewExperience createNewExperience = new CreateNewExperience(exName, exDescription, exTimeDate, exStreet,
                exCity, exState);
        createNewExperience.save();
    }

    public void checkDB(View view){
        CreateNewExperience last = CreateNewExperience.last(CreateNewExperience.class);
        String ex = last.experience_name + "-" + last.experience_description;
        Toast.makeText(getContext(),ex,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
