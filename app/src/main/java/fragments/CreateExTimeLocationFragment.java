package fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.experia.experia.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import models.Creation;
import util.CupboardDBHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class CreateExTimeLocationFragment extends Fragment {
    @BindView(R.id.btnPreview) Button gotoReviewBtn;
    @BindView(R.id.etDate) Button etDate;
    @BindView(R.id.etTime) Button etTime;
//    @BindView(R.id.query) Button checkBtn;
//    @BindView(R.id.btnAdvanced) Button moreDetailsBtn;

    //@BindView(R.id.iv_icon) ImageView logoImageView;
//    @BindView(R.id.tvDate) TextView tvDate;
//    @BindView(R.id.tvTime) TextView tvTime;
    PlaceAutocompleteFragment autocompleteFragment;
    static SQLiteDatabase db;

    private Unbinder unbinder;

    private static final String ARG_SECTION_NUMBER = "section-icon";
    private static final String ARG_SECTION_COLOR = "section-color";

    String exDate;
    String exTime;
    String exAddress;
    String exAddressName;
    LatLng exLocation;


    public static CreateExTimeLocationFragment newInstance(int color, int icon) {
        CreateExTimeLocationFragment fragment = new CreateExTimeLocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, icon);
        args.putInt(ARG_SECTION_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    private OnWhereAndWhenCompleteListener listener;

    // Define the events that the fragment will use to communicate
    public interface OnWhereAndWhenCompleteListener {
        public void onWhereAndWhenCompleted(String address, String addressName,  LatLng location, String date, String time);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWhereAndWhenCompleteListener) {
            listener = (OnWhereAndWhenCompleteListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnNameDescriptionPhotoCompleteListener");
        }
    }

    // Now we can fire the event when the user selects something in the fragment
    public void onSaveWhereAndWhen(View v) {
        exDate = etDate.getText().toString();
        exTime = etTime.getText().toString();


        System.out.println("DEBUGGY Exp 2 old: " + exDate + ", " + exTime + ", " + exAddress+ ", " + exAddressName + ", " + exLocation.toString());
        listener.onWhereAndWhenCompleted(exAddress, exAddressName, exLocation, exDate, exTime);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_time_location, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        getActivity().setTitle("Experience Details");

//        rootView.setBackgroundColor(ContextCompat.getColor(getContext(), getArguments().getInt(ARG_SECTION_COLOR)));
        //logoImageView.setImageResource(getArguments().getInt(ARG_SECTION_NUMBER));

        gotoReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveWhereAndWhen(v);
//                setTimeLocation(v);
            }
        });

//        checkBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkDB(v);
//            }
//        });



        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });


        CupboardDBHelper dbHelper = new CupboardDBHelper(getContext());
        db = dbHelper.getWritableDatabase();

        autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (place.getLatLng() != null) {
                    //Toast.makeText(getContext(), "GPS location was found!", Toast.LENGTH_SHORT).show();
                    exLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    exAddress = place.getAddress().toString();
                    exAddressName = place.getName().toString();
                }
            }

            @Override
            public void onError(Status status) {
                Log.i("fragment_time_location", "An error occurred: " + status);
            }
        });

        return rootView;
    }

    public void setTimeLocation(View view){
//         exDate = etDate.getText().toString();
         exTime = etTime.getText().toString();


        //set values obj
        ContentValues values = new ContentValues(1);
        values.put("date", exDate);
        //values.put("time", exTime);
        //values.put("address", exAddress);


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
