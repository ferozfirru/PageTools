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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.lang.Boolean.TRUE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment1 extends Fragment {

    Httpconnection conn;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    ExpandableListView expv;
    TextView txtscore,htitle;

    TextView numberResources;
    TextView numberHosts;
    TextView totalRequestBytes;
    TextView numberStaticResources;
    TextView htmlResponseBytes;
    TextView cssResponseBytes;
    TextView imageResponseBytes;
    TextView javascriptResponseBytes;
    TextView otherResponseBytes;
    TextView numberJsResources;
    TextView numberCssResources;
    TextView hdtxt;
    Button b1;
    EditText edturl;
    ToggleButton tb;
    LinearLayout ll;
    final String apikey = "AIzaSyCaMJvdWV3v69kZa87pOaniCHrxK460kWc";
    final String apiurl = "https://www.googleapis.com/pagespeedonline/v2/runPagespeed?";
    public MyFragment1() {
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
        View view = inflater.inflate(R.layout.fragment_my_fragment1, container, false);

        expv = (ExpandableListView) view.findViewById(R.id.explistview);
        progressBarHolder = (FrameLayout) view.findViewById(R.id.progressBarHolder);
//        String surl = getIntent().getStringExtra("url");
        txtscore = (TextView) view.findViewById(R.id.score);
        htitle = (TextView) view.findViewById(R.id.titleheading);
        numberResources = (TextView) view.findViewById(R.id.numberResources);
        numberHosts = (TextView) view.findViewById(R.id.numberHosts);
        totalRequestBytes = (TextView) view.findViewById(R.id.totalRequestBytes);
        numberStaticResources = (TextView) view.findViewById(R.id.numberStaticResources);
        htmlResponseBytes = (TextView) view.findViewById(R.id.htmlResponseBytes);
        cssResponseBytes = (TextView) view.findViewById(R.id.cssResponseBytes);
        imageResponseBytes = (TextView) view.findViewById(R.id.imageResponseBytes);
        javascriptResponseBytes = (TextView) view.findViewById(R.id.javascriptResponseBytes);
        otherResponseBytes = (TextView) view.findViewById(R.id.otherResponseBytes);
        numberJsResources = (TextView) view.findViewById(R.id.numberJsResources);
        numberCssResources = (TextView) view.findViewById(R.id.numberCssResources);
        edturl = (EditText) view.findViewById(R.id.urltext);
        tb = (ToggleButton) view.findViewById(R.id.toggleButton);
        hdtxt = (TextView) view.findViewById(R.id.hid_text);

        ll = (LinearLayout) view.findViewById(R.id.result_layout);
        ll.setVisibility(View.INVISIBLE);

        b1 = (Button) view.findViewById(R.id.pageinsightsbutton1);

        b1.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String type = "desktop";
                if(tb.isChecked()){
                    type = "mobile";
                }
                String Uurl = edturl.getText().toString();
                Log.d("entered",Uurl);

                if(!Uurl.isEmpty())
                {
                    if(!Uurl.startsWith("http"))
                    {
                        Uurl = "http://"+Uurl;
                        edturl.setText(Uurl.toLowerCase());
                    }
                    String surl = apiurl + "url="+Uurl+"&strategy="+type+"&key="+apikey;
                    Log.d("FullURL",surl);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new AsyncTaskRunner().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,surl);
                    }else{
                        new AsyncTaskRunner().execute(surl);
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


    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

            try {
                resp = conn.readUrl(params[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            JSONObject jobj = null;
            JSONObject jobjroot = null;
            try {
                jobj = new JSONObject(result).getJSONObject("formattedResults").getJSONObject("ruleResults");
                jobjroot = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String rule = null;

            String hdng = null;
            String format = null;
            float _rule;
            List<String> heading = new ArrayList<String>();
            List<String> hdn = new ArrayList<String>();
            HashMap<String,List<String>> hsmp = new HashMap<String,List<String>>();
            Iterator<String> iter = jobj.keys();
            int i=0;
            String group,final_group;
            ArrayList<Float> rules = new ArrayList<Float>();
            while (iter.hasNext()) {
                List<String> body = new ArrayList<String>();
                String key = iter.next();
                try {
                    String value = jobj.get(key).toString();
                    JSONObject jobj1 = new JSONObject(value);
                    hdng = jobj1.getString("localizedRuleName");
                    JSONObject summary = jobj1.getJSONObject("summary");
                    format = jobj1.getJSONObject("summary").getString("format");
                    rule = jobj1.getString("ruleImpact");
                    String Stringscore = jobjroot.getJSONObject("ruleGroups").getJSONObject("SPEED").getString("score");
                    int score = Integer.parseInt(Stringscore);
                    String title = jobjroot.getString("title");
                    htitle.setText(title);
                    txtscore.setText(Stringscore);

                    if(score < 70){
                        txtscore.setBackgroundColor(Color.parseColor("#FFA500"));
                    }else{
                        txtscore.setBackgroundColor(Color.parseColor("#32CD32"));
                    }

                    if(jobjroot.getJSONObject("pageStats").has("numberResources")){
                        String numberResources_v = jobjroot.getJSONObject("pageStats").getString("numberResources");
                        numberResources.setText(numberResources_v);
                    }

                    if(jobjroot.getJSONObject("pageStats").has("numberHosts")) {
                        String numberHosts_v = jobjroot.getJSONObject("pageStats").getString("numberHosts");
                        numberHosts.setText(numberHosts_v);
                    }
                    if(jobjroot.getJSONObject("pageStats").has("totalRequestBytes")) {
                        String totalRequestBytes_v = jobjroot.getJSONObject("pageStats").getString("totalRequestBytes");
                        totalRequestBytes.setText(totalRequestBytes_v);

                    }
                    if(jobjroot.getJSONObject("pageStats").has("numberStaticResources")) {
                        String numberStaticResources_v = jobjroot.getJSONObject("pageStats").getString("numberStaticResources");
                        numberStaticResources.setText(numberStaticResources_v);
                    }
                    if(jobjroot.getJSONObject("pageStats").has("htmlResponseBytes")) {
                        String htmlResponseBytes_v = jobjroot.getJSONObject("pageStats").getString("htmlResponseBytes");
                        htmlResponseBytes.setText(htmlResponseBytes_v);
                    }
                    if(jobjroot.getJSONObject("pageStats").has("cssResponseBytes")) {
                        String cssResponseBytes_v = jobjroot.getJSONObject("pageStats").getString("cssResponseBytes");
                        cssResponseBytes.setText(cssResponseBytes_v);
                    }
                    if(jobjroot.getJSONObject("pageStats").has("imageResponseBytes")) {
                        String imageResponseBytes_v = jobjroot.getJSONObject("pageStats").getString("imageResponseBytes");
                        imageResponseBytes.setText(imageResponseBytes_v);
                    }
                    if(jobjroot.getJSONObject("pageStats").has("javascriptResponseBytes")) {
                        String javascriptResponseBytes_v = jobjroot.getJSONObject("pageStats").getString("javascriptResponseBytes");
                        javascriptResponseBytes.setText(javascriptResponseBytes_v);
                    }
                    if(jobjroot.getJSONObject("pageStats").has("otherResponseBytes")) {
                        String otherResponseBytes_v = jobjroot.getJSONObject("pageStats").getString("otherResponseBytes");
                        otherResponseBytes.setText(otherResponseBytes_v);

                    }
                    if(jobjroot.getJSONObject("pageStats").has("numberJsResources")) {
                        String numberJsResources_v = jobjroot.getJSONObject("pageStats").getString("numberJsResources");
                        numberJsResources.setText(numberJsResources_v);
                    }
                    if(jobjroot.getJSONObject("pageStats").has("numberCssResources")) {
                        String numberCssResources_v = jobjroot.getJSONObject("pageStats").getString("numberCssResources");
                        numberCssResources.setText(numberCssResources_v);
                    }
                    _rule = Float.parseFloat(rule);

                    rules.add(_rule);
                    //if(_rule > 0){
//                        Log.d("RULEFNXxxx",Boolean.toString(jobj1.has("groups")));

                    if(jobj1.has("groups") == TRUE){
                        final_group = jobj1.getJSONArray("groups").get(0).toString();
                    }



                    String rURL = "";
                    if(summary.has("args")){
                        JSONArray sargs = summary.getJSONArray("args");
                        for (int sr=0;sr<sargs.length();sr++) {

                            String type = summary.getJSONArray("args").getJSONObject(sr).getString("type");
                            String rkey = summary.getJSONArray("args").getJSONObject(sr).getString("key");

                            rURL = summary.getJSONArray("args").getJSONObject(sr).getString("value");


                            if (type.equals("INT_LITERAL")) {
                                format = format.replace("{{"+rkey+"}}", rURL);
                            } else if (type.equals("HYPERLINK")) {
                                format = format.replace("{{BEGIN_LINK}}", "<a href=\"" + rURL + "\">");
                                format = format.replace("{{END_LINK}}", "</a>");
                            }
                        }
                    }

                    String Rdrctformat = "";
                    String Rdlnkkey = "";
                    String Rdlnk = "";
                    String Rdtype = "";
                    String fnrdrt = "";
                    String tmp1="";
                    JSONArray jargs;
                    if(jobj1.has("urlBlocks")) {
                        for (int irb = 0; irb < jobj1.getJSONArray("urlBlocks").length(); irb++) {
                            if(!jobj1.getJSONArray("urlBlocks").getJSONObject(irb).has("urls")) continue;
                            JSONArray redirectArray = jobj1.getJSONArray("urlBlocks").getJSONObject(irb).getJSONArray("urls");
                            for (int ir = 0; ir < redirectArray.length(); ir++) {
                                Rdrctformat = redirectArray.getJSONObject(ir).getJSONObject("result").getString("format");

                                jargs = redirectArray.getJSONObject(ir).getJSONObject("result").getJSONArray("args");
                                for (int iar = 0; iar < jargs.length(); iar++) {
                                    Rdlnkkey = jargs.getJSONObject(iar).getString("key");
                                    Rdlnk = jargs.getJSONObject(iar).getString("value");
                                    Rdtype = jargs.getJSONObject(iar).getString("type");
                                    if (Rdtype.equals("URL")) {
                                        Rdrctformat = Rdrctformat.replace("{{" + Rdlnkkey + "}}", "<a href=\"" + Rdlnk + "\">" + Rdlnk + "</a>");
                                    } else {
                                        //if(!Rdtype.isEmpty())
                                        Rdrctformat = Rdrctformat.replace("{{" + Rdlnkkey + "}}", Rdlnk);
                                    }
                                }
                                fnrdrt = fnrdrt + "<br>" + "<small>" + Rdrctformat + "</small>";


                            }
                        }
                    }
                    format = format + "<br>" + fnrdrt;
                    heading.add(hdng);

                    body.add(format);
                    hsmp.put(heading.get(i++),body);

                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progressBarHolder.setAnimation(outAnimation);
                    progressBarHolder.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                    hdtxt.setVisibility(View.GONE);
                } catch (JSONException e) {
                    // Something went wrong!
                }
                body = null;
            }


            ExplistAdapter expl = new ExplistAdapter(getActivity(),heading,hsmp);
            expl._rls = rules;
            expv.setAdapter(expl);
        }


        @Override
        protected void onPreExecute() {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            Log.d("febug","1");
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }



}
