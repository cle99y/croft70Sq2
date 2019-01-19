package com.geeklife.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.geeklife.croft70squadron.R;

public class User extends Fragment {

    // constant declarations
    final static Boolean USER_EXISTS = true;
    final static Boolean USER_NOT_SET = false;
    final static Boolean NO_ERRORS = false;
    final static Boolean HAS_ERRORS = true;
    final static String EMPTY = null;

    static SharedPreferences userInfo;
    static SharedPreferences.Editor userEditor;

    View v;
    Button actnBtn, clearBtn;
    TextView header, message, fName, lName, rank;
    TextView[] setUser;
    EditText newFName, newLName, newRank;
    EditText[] newUser;
    ViewGroup vg;
    Boolean isUserSet;
    InputMethodManager imm;

    public User() {
    }

    @Override
    public View onCreateView( final LayoutInflater inflater, final ViewGroup container,
                              Bundle savedInstanceState ) {

        userInfo = getContext().getSharedPreferences( "USER", Context.MODE_PRIVATE );
        userEditor = userInfo.edit();

        imm = ( InputMethodManager )
                getActivity().getSystemService( Context.INPUT_METHOD_SERVICE );


        isUserSet = userInfo.contains( "first_name" );

        v = inflater.inflate( R.layout.set_user, container, false );
        actnBtn = v.findViewById( R.id.btn_modify_user );
        clearBtn = v.findViewById( R.id.btn_clear );
        fName = v.findViewById( R.id.tv_set_first_name );
        lName = v.findViewById( R.id.tv_set_last_name );
        rank = v.findViewById( R.id.tv_set_rank );
        newFName = v.findViewById( R.id.et_new_first_name );
        newLName = v.findViewById( R.id.et_new_last_name );
        newRank = v.findViewById( R.id.et_new_rank );
        setUser = new TextView[]{fName, lName, rank};
        newUser = new EditText[]{newFName, newLName, newRank};

        setForm( isUserSet );


        actnBtn.setOnClickListener( new View.OnClickListener() {


            @Override
            public void onClick( View view ) {

                Boolean isError = NO_ERRORS;

                if ( isUserSet == USER_NOT_SET ) {
                    String fn = newFName.getText().toString();
                    String ln = newLName.getText().toString();
                    String r = newRank.getText().toString();

                    userEditor.putString( "first_name", fn );
                    userEditor.putString( "last_name", ln );
                    userEditor.putString( "rank", r );
                    userEditor.commit();
                }

                if ( isError == NO_ERRORS ) {
                    isUserSet = !isUserSet;
                    setForm( isUserSet );
                    imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
                }
            }
        } );

        clearBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                if ( isUserSet ) {
                    clearUserInfo();
                    clearUserForm();
                    isUserSet = USER_NOT_SET;
                    imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
                    setForm( isUserSet );
                } else {
                    clearUserForm();
                    setForm( isUserSet );
                }
            }
        } );

        return v;
    }

    private void clearUserInfo() {
        userEditor.remove( "first_name" );
        userEditor.remove( "last_name" );
        userEditor.remove( "rank" );
        userEditor.commit();
    }

    private void clearUserForm() {
        newFName.setText( "" );
        newLName.setText( "" );
        newRank.setText( "" );
        setForm( isUserSet );
    }


    private void setForm( Boolean userExists ) {
        if ( userExists ) {
            for ( int i = 0; i < newUser.length; i++ ) {
                setUser[i].setVisibility( View.VISIBLE );
                newUser[i].setVisibility( View.GONE );
                actnBtn.setText( R.string.modify );
            }
            fName.setText( userInfo.getString( "first_name", EMPTY ) );
            lName.setText( userInfo.getString( "last_name", EMPTY ) );
            rank.setText( userInfo.getString( "rank", EMPTY ) );
        } else {
            for ( int i = 0; i < newUser.length; i++ ) {
                newUser[i].setVisibility( View.VISIBLE );
                setUser[i].setVisibility( View.GONE );
                actnBtn.setText( R.string.submit );
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

    public SharedPreferences getUserInfo() {
        return userInfo;
    }


    public SharedPreferences.Editor getUserEditor() {
        return userEditor;
    }


}



