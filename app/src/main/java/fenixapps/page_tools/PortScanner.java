package fenixapps.page_tools;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class PortScanner extends Fragment {
    ArrayAdapter<String> arap;
    ArrayList<String> ars;
    ListView plst;
    String host;

    String[] ss;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TEST","TEST");
//        final int[] dports = {21,22,25,80,88,115,123,143,161,194,389,556,1080,1194,2082,8080,8081,6379,443,3306};

        final int[] dports = {21,22,3306,80,8080,8081,443,6379,35000};

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_port_scanner, container, false);

        final TextView urltext = (TextView) view.findViewById(R.id.portscanner_urltext);
        Button gobutton = (Button) view.findViewById(R.id.portscanner_gobutton);

        plst = (ListView) view.findViewById(R.id.port_listview);
        ars = new ArrayList<String>();

        gobutton.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


                String Uurl = urltext.getText().toString();
                Log.d("entered",Uurl);

                if(!Uurl.isEmpty())
                {
                    if(Uurl.startsWith("http"))
                    {
                        try {
                            URL hurl = new URL(Uurl);
                            host = hurl.getHost();
                        }catch (Exception e){

                        }

                    }else{
                        host = Uurl;
                    }

                    Log.d("host",host);

                    arap = new ArrayAdapter<String>(getActivity(), R.layout.portbody,R.id.postbodytext, ars);

                    plst.setAdapter(arap);

                    for (int ii = 0;ii < dports.length ; ii++) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new PortScanner.PortTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,String.valueOf(dports[ii]),host);
                        }else{
                            new PortScanner.PortTask().execute(String.valueOf(dports[ii]),host);
                        }


                            //            arap.notifyDataSetChanged();

                            //            if(){
                            //                ars.add("Port "+dports[ii]+" - Open");
                            //                Log.d("XXXXX",ars.toString());
                            //
                            //            }else{
                            //                ars.add("Port "+dports[ii]+" - Closed");
                            //                Log.d("XXXXX",ars.toString());
                            //
                            //            }
                    }


                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Enter Valid URL ",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

//        setContentView(R.layout.activity_port_scanner);
        Log.d("TEST","ZERO");

//        ArrayAdapter adap = new ArrayAdapter(getContext(),ars);


//        new AsyncTask<String,Void,Boolean>.execute(dports[ii],host);






/*
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TEST","ONE");
                // TODO Auto-generated method stub
//                startScan();
                for (int ii = 0;ii < dports.length ; ii++)
                {
                    if(socketCheck("103.11.85.212",dports[ii],1500)){
//                        ss[ii] = "Port "+dports[ii]+" - Open";
                        ars.add("Port "+dports[ii]+" - Open");
//                        Log.d("Port "+dports[ii],"TRUE");
                        Log.d("XXXXX",ars.toString());

                    }else{
//                        ss[ii] = "Port "+dports[ii]+" - Open";
                        ars.add("Port "+dports[ii]+" - Closed");
//                        Log.d("Port "+dports[ii],"FALSE");
                        Log.d("XXXXX",ars.toString());

                    }
//Log.d("SSS",ss.toString());
                }

            }
        });
        t.start();
*/
//        Log.d("XXXXX",ars.toString());



        return view;
    }


    private class PortTask extends AsyncTask<String, Void, Boolean> {
        int prt;
        protected Boolean doInBackground(String... params) {
            prt = Integer.parseInt(params[0]);
            Boolean result;
            String host = params[1];

            try {
                long start = System.currentTimeMillis();
                Socket socket = new Socket();
                InetSocketAddress addr = new InetSocketAddress(host, prt);
//                Log.d("NetworkUtil", "InetSocketAddress time taken: " + (System.currentTimeMillis() - start));

                start = System.currentTimeMillis();
                socket.connect(addr, 1000);
//                Log.d("NetworkUtil", "connect time taken: " + (System.currentTimeMillis() - start));
                socket.close();
                result =  true;
            } catch (IOException e) {
                e.printStackTrace();
                result =  false;
            }

            return result;
        }
@Override
        protected void onPreExecute() {
            arap.clear();
        }
@Override
        protected void onPostExecute(Boolean result) {
            if(result){
                ars.add("Port - "+prt+" is Open");
            }else{
                ars.add("Port - "+prt+" is Closed");
            }

            arap.notifyDataSetChanged();
        }
    }






    public static boolean socketCheck(String host, int port, int timeout) {
        try {
            long start = System.currentTimeMillis();
            Socket socket = new Socket();
            InetSocketAddress addr = new InetSocketAddress(host, port);
            Log.d("NetworkUtil", "InetSocketAddress time taken: " + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            socket.connect(addr, timeout);
            Log.d("NetworkUtil", "connect time taken: " + (System.currentTimeMillis() - start));
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
