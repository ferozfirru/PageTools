package fenixapps.page_tools;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PingCheck.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PingCheck#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PingCheck extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView lstv;
    ArrayList<String> lstarray;
    ArrayAdapter<String> aradap;
    Button b1;
    EditText pingedit;
    private OnFragmentInteractionListener mListener;
    private AsyncTask mTask;

    public PingCheck() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PingCheck.
     */
    // TODO: Rename and change types and number of parameters
    public static PingCheck newInstance(String param1, String param2) {
        PingCheck fragment = new PingCheck();
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ping_check, container, false);
        lstarray = new ArrayList<String>();
        aradap = new ArrayAdapter<String>(getActivity(),R.layout.pingbody,R.id.pngbodytxt,lstarray);

        lstv = (ListView) view.findViewById(R.id.pnglist);
        lstv.setEnabled(false);
//        Log.d("PING",ping("www.cartrade.com"));

        pingedit = (EditText) view.findViewById(R.id.pinghostedit);

        b1 = (Button) view.findViewById(R.id.pageinsightsbutton1);

        b1.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


                String Uurl = pingedit.getText().toString();
                Log.d("entered",Uurl);


                if(!Uurl.isEmpty())
                {
                    if(Uurl.startsWith("http"))
                    {
                        try {
                            URL ul = new URL(Uurl);
                            Uurl = ul.getHost();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }else
                    {

                    }
                    pingedit.setText(Uurl.toLowerCase());

//                    Log.d("FullURL",surl);

                    try{
                        mTask.cancel(true);
                    }catch (Exception e){

                    }
                     int hops = 10;
//                   String command = "ping -i 1 -Oc 5 " + Uurl;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        mTask = new PingCheck.AsyncTaskRunner().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"ping -i 1 -Oc "+hops+" " + Uurl);
                    }else{
                        mTask = new PingCheck.AsyncTaskRunner().execute("ping -c "+hops+" " + Uurl);
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Enter Valid Host ip/Domain Name ",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String str = "";
            try {
                Process process = Runtime.getRuntime().exec(params[0]);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        process.getInputStream()));
                Log.d("test",params[0]);
                int i;
                char[] buffer = new char[4096];
                StringBuffer output = new StringBuffer();
                while ((i = reader.read(buffer)) > 0){
                    Log.d("oup",output.toString());
                    output.append(buffer, 0, i);
                    publishProgress(output.toString());
                    if(output.length() > 1)
                        output.delete(0,i);

                }
                Log.d("oup2",output.toString());
                buffer = null;
                reader.close();

                // body.append(output.toString()+"\n");
                //str = output.toString();
                // Log.d(TAG, str);
            } catch (IOException e) {
                // body.append("Error\n");
                Log.d("testing","test");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            //txtv.setText(result);

            if(lstv.getCount()<1){
                aradap.add("Seems Invalid Host IP Or Domain Name..");
                aradap.notifyDataSetChanged();
                //lstv.getChildAt(0).setBackgroundColor(Color.GRAY);
            }

        }

        @Override
        protected void onPreExecute(){
            lstarray.clear();
            aradap.clear();
            lstv.setAdapter(null);
            lstv.setAdapter(aradap);
        }

        @Override
        protected void onProgressUpdate(String... values) {
//            lstarray.add(null);
            lstarray.add(values[0]);
            aradap.notifyDataSetChanged();
            if(lstarray.size() % 2 == 0){
//                lstv.getChildAt(0).setBackgroundColor(Color.GREEN);
//                lstv.setBackgroundColor(Color.GREEN);
            }else{
//                lstv.setBackgroundColor(Color.MAGENTA);
//                lstv.getChildAt(2).setBackgroundColor(Color.BLUE);

            }

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
}
