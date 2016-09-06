package fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import com.experia.experia.R;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Button etDate = (Button) getActivity().findViewById(R.id.etDate);
        String Month = (month < 10)? "0" + Integer.toString(month+1) : Integer.toString(month+1);
        String Day = (day < 10)? "0" + Integer.toString(day) : Integer.toString(day);
        String Year = Integer.toString(year);
        etDate.setText(Month+"/"+Day+"/"+Year);
    }
}

