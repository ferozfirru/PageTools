package fenixapps.page_tools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by fenix on 22/3/17.
 */

public class Httpconnection {

   public static String readUrl(String urlString) throws Exception {

       HttpURLConnection urlConnection = null;
       BufferedReader reader = null;

       // Will contain the raw JSON response as a string.
       String resp = null;

       try {
           // Construct the URL for the OpenWeatherMap query
           // Possible parameters are avaiable at OWM's forecast API page, at
           // http://openweathermap.org/API#forecast
           URL url = new URL(urlString);

//           SSLContext sslcontext = SSLContext.getInstance("TLSv1");
//
//           sslcontext.init(null,
//                   null,
//                   null);
//           SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
//
//           HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

           urlConnection = (HttpURLConnection) url.openConnection();

           urlConnection.setRequestMethod("GET");
           urlConnection.setConnectTimeout(2000);
           urlConnection.setRequestProperty("Connection", "close");
           urlConnection.connect();
//           Log.d("testeee",urlConnection.getHeaderFields().toString());

           // Read the input stream into a String
           InputStream inputStream = urlConnection.getInputStream();

           StringBuffer buffer = new StringBuffer();
           if (inputStream == null) {
               // Nothing to do.
               return null;
           }

           reader = new BufferedReader(new InputStreamReader(inputStream));

           String line;
           while ((line = reader.readLine()) != null) {
               // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
               // But it does make debugging a *lot* easier if you print out the completed
               // buffer for debugging.
               buffer.append(line + "\n");
           }

           if (buffer.length() == 0) {
               // Stream was empty.  No point in parsing.
               return null;
           }
           resp = buffer.toString();
           return resp;
       } catch (IOException e) {
           Log.e("PlaceholderFragment", "Error ", e);
           // If the code didn't successfully get the weather data, there's no point in attemping
           // to parse it.
           return null;
       } finally{
           if (urlConnection != null) {
               urlConnection.disconnect();
           }
           if (reader != null) {
               try {
                   reader.close();
               } catch (final IOException e) {
                   Log.e("PlaceholderFragment", "Error closing stream", e);
               }
           }
       }
   }
}

