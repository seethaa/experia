package fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.experia.experia.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by doc_dungeon on 8/27/16.
 */
public class CreateExTimeLocationFragment extends Fragment {
    @BindView(R.id.fire_event)
    Button saveBtn;
    @BindView(R.id.btnTimeDate) Button timeDateBtn;
    @BindView(R.id.btnTime) Button timeBtn;
    @BindView(R.id.query) Button checkBtn;
    @BindView(R.id.iv_icon)
    ImageView logoImageView;
    @BindView(R.id.etTimeDate)
    EditText etTimeDate;
    @BindView(R.id.etStreet) EditText etStreet;
    @BindView(R.id.etCity) EditText etCity;
    @BindView(R.id.etState) EditText etState;

    private Unbinder unbinder;

    private static final String ARG_SECTION_NUMBER = "section-icon";
    private static final String ARG_SECTION_COLOR = "section-color";

    public static CreateExTimeLocationFragment newInstance(int color, int icon) {
        CreateExTimeLocationFragment fragment = new CreateExTimeLocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, icon);
        args.putInt(ARG_SECTION_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_time_location, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        rootView.setBackgroundColor(ContextCompat.getColor(getContext(), getArguments().getInt(ARG_SECTION_COLOR)));
        logoImageView.setImageResource(getArguments().getInt(ARG_SECTION_NUMBER));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeLocation(v);
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDB(v);
            }
        });

        timeDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        etStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return rootView;
    }

    public void setTimeLocation(View view){



        String exTimeDate = etTimeDate.getText().toString();
        String exStreet = etStreet.getText().toString();
        String exCity = etCity.getText().toString();
        String exState = etState.getText().toString();

        /*Sugar ORM
        Creation creation = Creation.last(Creation.class);

        creation.experience_time_date = exTimeDate; // modify the values
        creation.experience_street_addr = exStreet;
        creation.experience_city= exCity;
        creation.experience_state = exState;
        creation.save(); // updates the previous entry with new values.
                 */
    }

    public void checkDB(View view){
        //Sugar ORM
        //Creation last = Creation.last(Creation.class);
        //String ex = last.experience_time_date + "-" + last.experience_street_addr  + "-"
       //         + last.experience_city  + "-" + last.experience_state;
        //Toast.makeText(getContext(),ex,Toast.LENGTH_SHORT).show();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerDialogFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerDialogFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
