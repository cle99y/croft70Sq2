package com.geeklife.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.geeklife.croft70squadron.R;

import java.util.Calendar;

public class Leave extends Fragment {

    EditText startDate, endDate;
    EditText reason;
    Button submit, clear;
    ImageView datePickerStart, datePickerEnd;
    DatePickerDialog datePicker;
    InputMethodManager imm;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        View v = inflater.inflate( R.layout.leave, container, false );

        imm = ( InputMethodManager )
                getActivity().getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );

        submit = v.findViewById( R.id.btn_submit );
        clear = v.findViewById( R.id.btn_clear );
        startDate = v.findViewById( R.id.date_from );
        endDate = v.findViewById( R.id.date_to );
        reason = v.findViewById( R.id.reason );
        datePickerStart = v.findViewById( R.id.dp_from );
        datePickerEnd = v.findViewById( R.id.dp_to );

        datePickerStart.requestFocus();


        // button click listeners

        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );

                String sd = startDate.getText().toString();
                String ed = endDate.getText().toString();
                String r = reason.getText().toString();

                Intent intent = new Intent( Intent.ACTION_SENDTO );
                intent.setData( Uri.parse( "mailto:paul.clegg@consultarc.com; paul.clegg101@mod.gov.uk" ) ); // only email apps should handle this
                intent.putExtra( Intent.EXTRA_SUBJECT, "new leave request" );
                intent.putExtra( Intent.EXTRA_TEXT, "START DATE: " + sd + "\n" +
                        " END DATE: " + ed + "\n" +
                        "\n" + " THE REASON GIVEN IS: " + "\n" + r + "\n" +
                        "\n" + "Sent from LXX Squadron Android App"
                );
                if ( intent.resolveActivity( getActivity().getPackageManager() ) != null ) {

                    clearForm();
                    startActivity( intent );
                }
            }
        } );

        clear.setOnClickListener( new View.OnClickListener() {
            // this listener clears the leave form when the CLEAR button is clicked
            @Override
            public void onClick( View view ) {
                imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
                clearForm();
                View formView = getActivity().getCurrentFocus();
                if ( formView != null ) formView.clearFocus();


            }
        } );

        // date pickers

//        datePickerStart.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick( View view ) {
//                Log.i( "DATE", "CLICKED" );
//                DateListener( startDate, datePickerStart );
//            }
//        } );

        datePickerStart.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch( View view, MotionEvent motionEvent ) {
                DateListener( startDate, datePickerStart );
                return false;
            }
        } );

        datePickerEnd.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch( View view, MotionEvent motionEvent ) {
                DateListener( endDate, datePickerEnd );
                return false;
            }
        } );

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
    }


    private void clearForm() {
        startDate.getText().clear();
        endDate.getText().clear();
        reason.getText().clear();
    }

    public void DateListener( final EditText et, final ImageView iv ) {

        iv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get( Calendar.YEAR );
                int mm = calendar.get( Calendar.MONTH );
                int dd = calendar.get( Calendar.DAY_OF_MONTH );
                DatePickerDialog datePicker = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
                        String date = padDate( dayOfMonth ) + "/" + padDate( monthOfYear + 1 )
                                + "/" + String.valueOf( year );
                        et.setText( date );


                    }
                }, yy, mm, dd );

                datePicker.show();

            }
        } );

    }

    private String padDate( int val ) {


        if ( val < 10 ) {
            return "0" + String.valueOf( val );
        }

        return String.valueOf( val );
    }


}


