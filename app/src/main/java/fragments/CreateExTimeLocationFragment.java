package fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;
import android.widget.Toast;

import com.experia.experia.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import models.Creation;
import util.CupboardDBHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class CreateExTimeLocationFragment extends Fragment {
    @BindView(R.id.fire_event) Button saveBtn;
    @BindView(R.id.btnDate) Button dateBtn;
    @BindView(R.id.btnTime) Button timeBtn;
    @BindView(R.id.query) Button checkBtn;
    //@BindView(R.id.iv_icon) ImageView logoImageView;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.etStreet) EditText etStreet;
    static SQLiteDatabase db;

    private Unbinder unbinder;

    private static final String ARG_SECTION_NUMBER = "section-icon";
    private static final String ARG_SECTION_COLOR = "section-color";

    String exDate;
    String exTime;
    String exAddress;

    private OnWhereAndWhenCompleteListener listener;

    // Define the events that the fragment will use to communicate
    public interface OnWhereAndWhenCompleteListener {
        public void onWhereAndWhenCompleted(String address, String date, String time);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWhereAndWhenCompleteListener) {
            listener = (OnWhereAndWhenCompleteListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnNameAndDescriptionCompleteListener");
        }
    }

    // Now we can fire the event when the user selects something in the fragment
    public void onSaveWhereAndWhen(View v) {
        exDate = tvDate.getText().toString();
        exTime = tvTime.getText().toString();
        exAddress = etStreet.getText().toString();


        System.out.println("DEBUGGY Exp 2 old: " + exDate + ", " + exTime + ", " + exAddress);
        listener.onWhereAndWhenCompleted(exAddress, exDate, exTime);
    }

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
        //logoImageView.setImageResource(getArguments().getInt(ARG_SECTION_NUMBER));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveWhereAndWhen(v);
//                setTimeLocation(v);
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDB(v);
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
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
                dateBtn.setVisibility(View.VISIBLE);
                tvDate.setVisibility(View.VISIBLE);
                timeBtn.setVisibility(View.VISIBLE);
                tvTime.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvTime.addTextChangedListener(new TextWatcher() {
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

        tvDate.addTextChangedListener(new TextWatcher() {
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

        CupboardDBHelper dbHelper = new CupboardDBHelper(getContext());
        db = dbHelper.getWritableDatabase();

        return rootView;
    }

    public void setTimeLocation(View view){
         exDate = tvDate.getText().toString();
         exTime = tvTime.getText().toString();
         exAddress = etStreet.getText().toString();


        //set values obj
        ContentValues values = new ContentValues(1);
        values.put("date", exDate);
        //values.put("time", exTime);
        values.put("address", exAddress);


        // update first record
        cupboard().withDatabase(db).update(Creation.class, values, "_id = ?", "1");

    }

    public void checkDB(View view){
        //Cupboard ORM
        Creation creation = cupboard().withDatabase(db).query(Creation.class).get();
        String entry = creation.title +"/" + creation.description+"/" + creation.date +"-"+ creation.address;
        Toast.makeText(getContext(),entry,Toast.LENGTH_SHORT).show();

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