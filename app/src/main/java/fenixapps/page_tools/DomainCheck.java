package fenixapps.page_tools;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DomainCheck.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DomainCheck#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DomainCheck extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DomainCheck() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DomainCheck.
     */
    // TODO: Rename and change types and number of parameters
    public static DomainCheck newInstance(String param1, String param2) {
        DomainCheck fragment = new DomainCheck();
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
        View view = inflater.inflate(R.layout.fragment_domain_check, container, false);
new DomainCheck.DomainAsync().execute();
        return view;
    }

    private class DomainAsync extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
Log.d("TESTING","OKAY");
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            OkHttpClient client = new OkHttpClient();
            String myKey = "2s7YSBBy4S_LRuwXcRCycnzpir1KQ4pDJ";
            String mySecret = "LRv1zeeFWk824gfgPbL4Te";

//            String url ="https://api.godaddy.com/v1/domains/available?checkType=FAST";
            String url ="https://api.ote-godaddy.com/v1/domains/available?checkType=full";

            String json = "[\"tesingee.club\",\"tellingee.com\",\"jindeingee.xyz\"]";
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                    .addHeader("Authorization", "sso-key "+myKey+":"+mySecret)
//                    .addHeader("X-Shopper-Id", $myID)
                    .addHeader("Accept", "application/json")

                    .url(url)
                        .post(body)
                        .build();
                Response response = null;
                try {

                    response = client.newCall(request).execute();
                    Log.d("DADDY1",response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d("DADDY",response.body().string().toString());
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
