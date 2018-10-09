package fenixapps.page_tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLPeerUnverifiedException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Http2check extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PingCheck.OnFragmentInteractionListener mListener;

    public AsyncTask mTask;
    private String mParam1;
    private String mParam2;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    ArrayList<H2Object> h2objects;
    H2Adapter h2dap;
    ImageView h2indi;
    TextView h2msg;
    ListView h2list;
    Button b1;
    EditText edturl;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PingCheck.
     */
    // TODO: Rename and change types and number of parameters
    public static Http2check newInstance(String param1, String param2) {
        Http2check fragment = new Http2check();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.layout_http2check, container, false);

        progressBarHolder = (FrameLayout) view.findViewById(R.id.h2progressBarHolder2);
        b1 = (Button) view.findViewById(R.id.h2button1);
        edturl = (EditText) view.findViewById(R.id.h2hostedit);

        h2msg = (TextView) view.findViewById(R.id.h2msg);
        h2indi = (ImageView) view.findViewById(R.id.h2indicator);
        h2list = (ListView) view.findViewById(R.id.h2headerlist);

        h2indi.setVisibility(View.INVISIBLE);
        h2msg.setText("Enter Domain name..");

        h2objects = new ArrayList<H2Object>();
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

                    if(!furl.startsWith("http") || !furl.startsWith("Http")) {

                        URL url = null;
                        try {
                            url = new URL(Uurl);
                            furl = url.getHost();

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        furl = "https://" + furl;
                        Log.d("dbg",furl);
                    }

                    String surl = furl;

                    Log.d("FullURL",surl);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new Http2check.AsyncTaskRunner().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,surl);
                    }else{
                        new Http2check.AsyncTaskRunner().execute(surl);
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Enter Valid URL ",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        return view;

    }




    private class AsyncTaskRunner extends AsyncTask<String, String, Response> {

        @Override
        protected Response doInBackground(String... params) {

            Response response = null;
            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                System.out.println("url: " + chain.request().url());
                                return chain.proceed(chain.request());
                            }
                        })
                        .followRedirects(Boolean.TRUE)
                        .build();
                Request request = new Request.Builder()
                        .url(params[0].toLowerCase())
                        .head()
                        .build();

                response = client.newCall(request).execute();
                Log.d("CIphers1",response.handshake().cipherSuite().toString());
                Log.d("CIphers2",response.handshake().peerCertificates().toString());
                Log.d("CIphers3",response.handshake().peerCertificates().get(0).toString());

                Pattern p1 = Pattern.compile("Not Before:(.*)");
                Pattern p2 = Pattern.compile("Not After :(.*)");

                String Cert = response.handshake().peerCertificates().get(0).toString();
                Matcher m1 = p1.matcher(Cert);
                Matcher m2 = p2.matcher(Cert);
                if(m1.find())
                {
                    Log.d("Valid From:",m1.group(1));
                }
                if(m2.find())
                {
                    Log.d("Valid Upto:",m2.group(1));
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
//                Log.d("dbg1",response.toString());

            }
            if (!response.isSuccessful()) try {
                throw new IOException("Unexpected code " + response);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("dbg","3");
            }

            return response;
/*
            try {
                System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            /*try {
                URL obj = new URL("https://google.com");
                URLConnection conn = obj.openConnection();
                Log.d("testeee",conn.getHeaderFields().toString());
                Map<String, List<String>> map = conn.getHeaderFields();
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    Log.d("Key : " + entry.getKey().toString(), " ,Value : " + entry.getValue().toString());
                }

//                System.out.println("\nGet Response Header By Key ...\n");
                String server = conn.getHeaderField("Server");

                if (server == null) {
//                    System.out.println("Key 'Server' is not found!");
                    Log.d("Server - " , "NO");

                } else {
                    Log.d("Server - " , server);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("ABC","ABAC");
                e.printStackTrace();
            } */

        }

        @Override
        protected void onPostExecute(Response response) {

            //Log.d("PROTO",response.toString());
            h2indi.setVisibility(View.VISIBLE);

            final int sdk = android.os.Build.VERSION.SDK_INT;

            if(response != null) {

                String Proto = response.protocol().toString();
                if(Proto.equals("h2")){
                    h2msg.setText("Wow ! Its Supports HTTP2 :)");

                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        h2indi.setBackgroundDrawable( getResources().getDrawable(R.drawable.tick) );
                    } else {
                        h2indi.setBackground( getResources().getDrawable (R.drawable.tick));
                    }
                }else{
                    h2msg.setText("Oh ! Its Doesn't Support HTTP2 :(");
                    h2list.setAdapter(null);
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            h2indi.setBackgroundDrawable( getResources().getDrawable(R.drawable.cross) );
                        } else {
                            h2indi.setBackground( getResources().getDrawable (R.drawable.cross));
                        }

                     }
                     h2objects.clear();
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d(responseHeaders.name(i), responseHeaders.value(i));
                    h2objects.add(new H2Object(responseHeaders.name(i), responseHeaders.value(i)));
                }
                h2dap = new H2Adapter(getActivity(), h2objects);

                h2list.setAdapter(null);
                h2list.setAdapter(h2dap);
            }else{
                h2msg.setText("Oh ! Its Doesn't Support HTTP2 :(");
                h2list.setAdapter(null);

                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    h2indi.setBackgroundDrawable( getResources().getDrawable(R.drawable.cross) );
                } else {
                    h2indi.setBackground( getResources().getDrawable (R.drawable.cross));
                }
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {

            h2msg.setText("Loading....");

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}