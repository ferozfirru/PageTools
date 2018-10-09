package fenixapps.page_tools;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private ListView navList;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        navList = (ListView)findViewById(R.id.navlist);
        ArrayList<String> navArray = new ArrayList<String>();
        navArray.add("Home");
        navArray.add("Page Insights");
        navArray.add("Dns checker");
        navArray.add("Open-port checker");
        navArray.add("Ping checker");
        navArray.add("SSL checker");
        navArray.add("Forward DNS records (A)");

        navArray.add("Sub domain checker");
        navArray.add("Up/Down");
        navArray.add("Traceroute");
        navArray.add("IP Blacklist");
        navArray.add("Who-IS");
        navArray.add("Page size");

        navArray.add("Page hash");
        navArray.add("Header check");
        navArray.add("hashings");
        navArray.add("http2 check");
        navArray.add("all page links");
        navList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,navArray);
        navList.setAdapter(adapter);
        navList.setOnItemClickListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.opendrawer,R.string.closedrawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();

        loadSelection(0);
    }

    private void loadSelection(int i){
        navList.setItemChecked(i,true);

        switch (i){
            case 0:

                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,homeFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case 1:

                MyFragment1 myFragment1 = new MyFragment1();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,myFragment1);
                fragmentTransaction.commitAllowingStateLoss();
                break;

            case 2:
                MyFragment2 myFragment2 = new MyFragment2();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,myFragment2);
                fragmentTransaction.commitAllowingStateLoss();
                break;

            case 3:

                PortScanner myFragment3 = new PortScanner();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,myFragment3);
                fragmentTransaction.commitAllowingStateLoss();
                break;


            case 4:
                PingCheck myFragment4 = new PingCheck();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,myFragment4);
                fragmentTransaction.commitAllowingStateLoss();

                break;

            case 5:
                Http2check myFragment5 = new Http2check();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,myFragment5);
                fragmentTransaction.commitAllowingStateLoss();
                break;

            case 6:
                DomainCheck myFragment6 = new DomainCheck();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder,myFragment6);
                fragmentTransaction.commitAllowingStateLoss();
                break;

        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.string.action_settings) {
            return true;
        }else if (id == android.R.id.home){
            if (drawerLayout.isDrawerOpen(navList)){
                drawerLayout.closeDrawer(navList);
            }else{
                drawerLayout.openDrawer(navList);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadSelection(position);

        drawerLayout.closeDrawer(navList);
    }
}
