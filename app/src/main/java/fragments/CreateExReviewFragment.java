package fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.experia.experia.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateExReviewFragment extends Fragment {
    @BindView(R.id.btnSubmit) Button saveBtn;
//    @BindView(R.id.etDate) EditText etDate;
    @BindView(R.id.ivExperienceImage) ImageView ivImage;

    @BindView(R.id.tvDayAtTime)
    TextView etTime;
    @BindView(R.id.tvDate) TextView etDate;

    @BindView(R.id.tvExperienceTitle) TextView etTitle;

    @BindView(R.id.tvDescription) TextView etDescription;
    @BindView(R.id.tvLocationAddress) TextView etAddress;
    @BindView(R.id.tvLocationName) TextView etAddressName;
    @BindView(R.id.tvSpotsLeft) TextView etNumGuests;
    boolean populated = false;

    private Calendar mCalendar;
    private DatePickerDialog.OnDateSetListener mDateListener;

    static SQLiteDatabase db;

    private Unbinder unbinder;

    private static final String ARG_SECTION_NUMBER = "section-icon";
    private static final String ARG_SECTION_COLOR = "section-color";


    String mUserDisplayName;
    String mTitle;
    String mBody;
    int mNumGuests;
    String mDate;
    String mTime;
    String mDuration;
    String mTags;
    String mImgURL;
    String mAddress;
    String mAddressName;
    double mLatitude;
    double mLongitude;
    int mType;


    // newInstance constructor for creating fragment with arguments
    public static CreateExReviewFragment newInstance(int color, int icon) {
        CreateExReviewFragment fragment = new CreateExReviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, icon);
        args.putInt(ARG_SECTION_COLOR, color);
        fragment.setArguments(args);

        return fragment;
    }

    private OnReviewCompleteListener listener;

    public void populatePreviewFields(String mUserDisplayName, String mTitle, String mBody, int mNumGuests, String mDate, String mTime, String mDuration, String mTags, String mImgURL, String mAddress, String mAddressName, double mLatitude, double mLongitude, int mType) {

        System.out.println("DEBUGGY GOT TO POP");
        this.mUserDisplayName = mUserDisplayName;
        this.mTitle = mTitle;
        this.mBody = mBody;
        this.mNumGuests = mNumGuests;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mImgURL = mImgURL;
        this.mAddress = mAddress;
        this.mAddressName = mAddressName;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mType = mType;
        populated = true;


        etTime.setText(mTime);

        etTitle.setText(mTitle);

        etDescription.setText(mBody);
        etAddress.setText(mAddress);
        etAddressName.setText(mAddressName);
        etNumGuests.setText(mNumGuests+"");
        Glide.with(getActivity()).load(mImgURL).centerCrop().placeholder(R.drawable.icon_take_photo)
                .into(ivImage);

    }

    // Define the events that the fragment will use to communicate
    public interface OnReviewCompleteListener {
       public void OnReviewCompleted(String mUserDisplayName, String mTitle, String mBody, int mNumGuests, String mDate, String mTime, String mDuration, String mTags, String mImgURL, String mAddress, double mLatitude, double mLongitude, int mType);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReviewCompleteListener) {
            listener = (OnReviewCompleteListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnNameDescriptionPhotoCompleteListener");
        }
    }

    // Now we can fire the event when the user selects something in the fragment
    public void onSaveReview(View v) {

        mTitle = etTitle.getText().toString();
        mBody = etDescription.getText().toString();
        mNumGuests = Integer.parseInt(etNumGuests.getText().toString());
//        mDate = ;
        mTime = etTime.getText().toString();
//        mImgURL = mImgURL;
        //mAddress = etAddress.getText().toString();
//        mType = mType;


//        System.out.println("DEBUGGY Exp 2 old: " + exDate + ", " + exTime + ", " + exAddress);
        listener.OnReviewCompleted(mUserDisplayName, mTitle, mBody,mNumGuests,mDate,mTime, mDuration,mTags, mImgURL,
                mAddress, mLatitude, mLongitude, mType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Preview Experience");

        System.out.println("DEBUGGY GOT TO CREATE VIEW");

        View rootView = inflater.inflate(R.layout.fragment_create_review, container, false);
        unbinder = ButterKnife.bind(this, rootView);

//        rootView.setBackgroundColor(ContextCompat.getColor(getContext(), getArguments().getInt(ARG_SECTION_COLOR)));
        //logoImageView.setImageResource(getArguments().getInt(ARG_SECTION_NUMBER));


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveReview(v);
            }
        });


        //time on click listener
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        //date on click listener
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCalendar = Calendar.getInstance();

                mDateListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        mCalendar.set(Calendar.YEAR, year);
                        mCalendar.set(Calendar.MONTH, monthOfYear);
                        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        //update date in edittext
                        String myFormat = "MM-dd-yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        mDate = sdf.format(mCalendar.getTime());
                        etDate.setText(mDate);



                    }

                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mDateListener, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                //disable all past dates
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();


            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("DEBUGGY GOT TO onstart");


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
