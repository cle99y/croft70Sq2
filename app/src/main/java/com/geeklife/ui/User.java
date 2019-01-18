package com.geeklife.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.geeklife.croft70squadron.R;

public class User extends Fragment {

    final static Boolean USER_EXISTS = true;
    final static Boolean USER_NOT_SET = false;
    View v;
    Button btn;
    TextView header, message, fName, lName, rank;
    TextView[] setUser;
    EditText newFName, newLName, newRank;
    EditText[] newUser;
    ViewGroup vg;
    SharedPreferences userInfo;
    SharedPreferences.Editor userEditor;
    Boolean isUserSet;

    @Override
    public View onCreateView( final LayoutInflater inflater, final ViewGroup container,
                              Bundle savedInstanceState ) {

        isUserSet = false;

        v = inflater.inflate( R.layout.set_user, container, false );
        btn = v.findViewById( R.id.btn_modify_user );
        fName = v.findViewById( R.id.tv_set_first_name );
        lName = v.findViewById( R.id.tv_set_last_name );
        rank = v.findViewById( R.id.tv_set_rank );
        newFName = v.findViewById( R.id.et_new_first_name );
        newLName = v.findViewById( R.id.et_new_last_name );
        newRank = v.findViewById( R.id.et_new_rank );
        setUser = new TextView[]{fName, lName, rank};
        newUser = new EditText[]{newFName, newLName, newRank};

        setForm( isUserSet );


        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                toggleVisibility();


            }
        } );

        return v;

    }

    private void setForm( Boolean userExists ) {
        if ( userExists ) {
            for ( int i = 0; i < newUser.length; i++ ) {
                setUser[i].setVisibility( View.VISIBLE );
                newUser[i].setVisibility( View.GONE );
                btn.setText( R.string.modify );
            }
        } else {
            for ( int i = 0; i < newUser.length; i++ ) {
                newUser[i].setVisibility( View.VISIBLE );
                setUser[i].setVisibility( View.GONE );
                btn.setText( R.string.submit );
            }
        }
    }

    private void toggleVisibility() {
        for ( int i = 0; i < newUser.length; i++ ) {
            if ( newUser[i].getVisibility() == View.VISIBLE ) {
                newUser[i].setVisibility( View.GONE );
                setUser[i].setVisibility( View.VISIBLE );
            } else {
                newUser[i].setVisibility( View.VISIBLE );
                setUser[i].setVisibility( View.GONE );
            }
        }
    }
}



