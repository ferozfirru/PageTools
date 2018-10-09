package fenixapps.page_tools;

/**
 * Created by fenix on 3/4/17.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import static java.lang.Boolean.TRUE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment2 extends Fragment {
//    http://api.hackertarget.com/hostsearch/?q=cartrade.com
    ListView dnslistView;
    Httpconnection conn;
    ArrayList<String> ars ;
    ArrayList<DnsObject> objects;
    DnsAdapter customAdapter;
    Button b1;
    EditText edturl;
    String apiurl = "https://api.hackertarget.com/dnslookup/?q=";
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    public MyFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_fragment2, container, false);
        progressBarHolder = (FrameLayout) view.findViewById(R.id.progressBarHolder2);
        b1 = (Button) view.findViewById(R.id.dnsbutton);
        edturl = (EditText) view.findViewById(R.id.urltextdns);
        b1.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String Uurl = edturl.getText().toString();
                Log.d("entered",Uurl);

                if(!Uurl.isEmpty())
                {
                    String furl = Uurl;

                    if(furl.startsWith("http") || furl.startsWith("Http")) {

                        URL url = null;
                        try {
                            url = new URL(Uurl);
                            furl = url.getHost();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }

//                    edturl.setText(furl);
                    String surl = apiurl+furl+"&output=json";
                    Log.d("FullURL",surl);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new MyFragment2.AsyncTaskRunner().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,surl);
                    }else{
                        new MyFragment2.AsyncTaskRunner().execute(surl);
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Enter Valid URL ",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String>{
        private String resp;

        @Override
        protected String doInBackground(String... params) {
            try {
                resp = conn.readUrl(params[0]);

//                resp = "cartrade.com.\t\t600\tIN\tA\t103.11.86.55\n" +
//                        "cartrade.com.\t\t3600\tIN\tNS\tpdns09.domaincontrol.com.\n" +
//                        "cartrade.com.\t\t3600\tIN\tNS\tpdns10.domaincontrol.com.\n" +
//                        "cartrade.com.\t\t600\tIN\tSOA\tpdns09.domaincontrol.com. dns.jomax.net. 2017033102 28800 7200 604800 600\n" +
//                        "cartrade.com.\t\t600\tIN\tMX\t5 alt1.aspmx.l.google.com.\n" +
//                        "cartrade.com.\t\t600\tIN\tMX\t5 alt2.aspmx.l.google.com.\n" +
//                        "cartrade.com.\t\t600\tIN\tMX\t5 alt3.aspmx.l.google.com.\n" +
//                        "cartrade.com.\t\t600\tIN\tMX\t5 alt4.aspmx.l.google.com.\n" +
//                        "cartrade.com.\t\t600\tIN\tTXT\t\"google-site-verification=UrbFOiSKcxQpodeykHz2dEn7HYlmvFakPWMtC9p1cMA\"\n" +
//                        "cartrade.com.\t\t600\tIN\tTXT\t\"google-site-verification=vBUBoMACJFLysGkGWW2OtGIS9vs1OodSA6pxEVwKXO4\"\n" +
//                        "cartrade.com.\t\t600\tIN\tTXT\t\"google-site-verification: google8728895e8f62db64.html\"\n" +
//                        "cartrade.com.\t\t600\tIN\tTXT\t\"google-site-verification=U99dI8fdRLIdxNUP_TuAGBtPyLKV0XedjK5zK4C8Ix4\"\n" +
//                        "cartrade.com.\t\t600\tIN\tTXT\t\"google-site-verification=y5C1GkR0fFCbBhLer5rnG2Ov-l8d1Bo_g1iLiKp_chs\"\n" +
//                        "cartrade.com.\t\t600\tIN\tTXT\t\"v=spf1 ip4:103.11.85.30/28 ip4:103.11.85.32/29 ip4:103.11.85.211/28 ip4:103.11.85.224/27 ip4:103.11.85.145/32 ip4:103.11.85.16/32 ip4:103.11.85.123/29 ip4:103.11.85.169/32 ip4:103.11.86.60/29 include:_spf.google.com -all\"";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result){
Log.d("DEBUG",result);
//            Log.d("CHECK",Boolean.toString(result.trim().equals("no records found")));
            if(result.trim().equals("no records found"))
            {

            }else {
                StringTokenizer st = new StringTokenizer(result, "\n");
                StringTokenizer st2;
                objects = new ArrayList<DnsObject>();
                ars = new ArrayList<String>();
                while (st.hasMoreTokens()) {
                    int i = 0;
//            System.out.println(st.nextToken());
                    st2 = new StringTokenizer(st.nextToken(), "\t");
                    while (st2.hasMoreTokens()) {
                        ars.add(i++, st2.nextToken().toString());
                    }
                    objects.add(new DnsObject(ars.get(3), ars.get(2), ars.get(1), ars.get(4)));
                }
//            Log.d("OBJECTS",objects.toString());
                customAdapter = new DnsAdapter(getActivity(), objects);
                dnslistView = (ListView) getView().findViewById(R.id.dns_listview);
                dnslistView.setAdapter(null);
//            dnslistView.getAdapter().notify();
                dnslistView.setAdapter(customAdapter);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
        @Override
        protected void onPreExecute() {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }
    }



}
