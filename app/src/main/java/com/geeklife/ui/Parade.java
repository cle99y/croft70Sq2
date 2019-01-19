package com.geeklife.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.geeklife.common.IntenetTest;
import com.geeklife.croft70squadron.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Parade extends Fragment {

    static final String BTN_UNIFORM = "Show Uniform Details";
    static final String BTN_PARADE = "Show Parade Details";
    static final String LOAD_UNIFORM = "Loading Uniform Information. . .";
    static final String LOAD_PARADE = "Loading Parade Details. . .";
    static final String URL_PARADE = "http://www.lxxsquadron.com/cadets-staff/details-for-next-parade/";
    static final String URL_UNIFORM = "http://www.lxxsquadron.com/cadets-staff/uniforms/";
    WebView webview;
    String url, paradeLoad, uniformLoad, btnParade, btnUniform;
    TextView progress;
    Button btn;
    Boolean isUniform;      // true: show Uniforms, false: show Parade
    SharedPreferences paradeData, userInfo;
    SharedPreferences.Editor editor, userEditor;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.parade, container, false );

        userInfo = getContext().getSharedPreferences( "USER", Context.MODE_PRIVATE );
        userEditor = userInfo.edit();


        paradeData = getActivity().getSharedPreferences( "PARADE", Context.MODE_PRIVATE );
        editor = paradeData.edit();
        webview = v.findViewById( R.id.webView );
        progress = v.findViewById( R.id.progress );
        btn = v.findViewById( R.id.button );
        isUniform = false;
        url = URL_PARADE;

        if ( userInfo.contains( "first_name" ) ) {
            Toast.makeText( getContext(),
                    "Welcome back " +
                            userInfo.getString( "rank", null ) + " " +
                            userInfo.getString( "first_name", null ) + " " +
                            userInfo.getString( "last_name", null ) + "!",
                    Toast.LENGTH_LONG ).show();
        }


        if ( IntenetTest.testConnection( getActivity() ) ) {
            new GetWebData().execute();
        } else {
            btn.setText( "RETRY" );
            webview.setVisibility( View.GONE );
            progress.setText( R.string.no_internet );
        }

        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                isUniform = !isUniform;  // toggle the boolean variable

                if ( isUniform ) {
                    url = URL_UNIFORM;
                    progress.setText( LOAD_UNIFORM );
                } else {
                    url = URL_PARADE;
                    progress.setText( LOAD_PARADE );
                }
                if ( IntenetTest.testConnection( getContext() ) ) {
                    new GetWebData().execute();
                } else {
                    progress.setText( R.string.no_internet );
                    btn.setText( "retry" );
                }

            }
        } );

        return v;
    }


    private class GetWebData extends AsyncTask<Void, Void, Document> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            webview.setVisibility( View.GONE );
            progress.setVisibility( View.VISIBLE );
            btn.setVisibility( View.GONE );

        }

        @Override
        protected Document doInBackground( Void... voids ) {

            Document document = null;
            try {
                document = Jsoup.connect( url ).get();
                String date = document.select( "td" ).get( 1 ).toString();
                date = getDateString( date );
                Log.i( "DATE", date + " substring" );
                document.getElementById( "masthead" ).remove();
                document.getElementById( "colophon" ).remove();
                document.getElementById( "secondary" ).remove();
                if ( !isUniform )
                    document.select( "tr" ).last().html( "<td colspan='2'> </br></br>Bring 3822 & sports kit to ALL parades.\n" +
                            "</br></br>To qualify for flying & camps, all cadets must now have 60% attendance in the 3 months prior to the date of activity.\n" +
                            "\n" +
                            "</br></br>Leave requests must be booked through this app, the website or leave request form. Failure to do so may result in you being marked as AWOL.\n" +
                            "\n" +
                            "</br></br>Requests for new uniform should be posted in the post box via the uniform request slips.</td>" );

                editor.putString( getKey(), document.toString() );
                editor.commit();

            } catch ( IOException e ) {
                e.printStackTrace();
            }
            return document;
        }

        @Override
        protected void onPostExecute( Document document ) {
            super.onPostExecute( document );

            webview.loadDataWithBaseURL( url, document.toString(), "text/html", "utf-8", "" );
            webview.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );

            webview.setWebViewClient( new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading( WebView view, WebResourceRequest request ) {
                    view.loadUrl( url );
                    return super.shouldOverrideUrlLoading( view, request );
                }
            } );

            progress.setVisibility( View.GONE );
            webview.setVisibility( View.VISIBLE );
            btn.setVisibility( View.VISIBLE );

            if ( isUniform ) {
                btn.setText( BTN_PARADE );

            } else {
                btn.setText( BTN_UNIFORM );

            }

        }

        private String getKey() {
            if ( isUniform ) return "uniform";
            return "parade";
        }

        private String getDateString( String d ) {
            String out = "";
            if ( d.contains( "<TD>" ) || d.contains( "<td>" ) ) {
                out = d.substring( d.indexOf( ">" ) + 1, d.indexOf( "</" ) );
                if ( out.contains( ";" ) ) return out.substring( out.indexOf( ";" ) + 1 );
            }

            return out;
        }
    }
}
