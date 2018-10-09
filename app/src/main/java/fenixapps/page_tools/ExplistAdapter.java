package fenixapps.page_tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fenix on 24/3/17.
 */

public class ExplistAdapter extends BaseExpandableListAdapter {
    private List<String> heads ;
    HashMap <String,List<String>> bdy = new HashMap<String,List<String>>();
    ArrayList<Float> _rls = new ArrayList<Float>();
    private Context con;
    ExplistAdapter(Context con,List<String> heads,HashMap <String,List<String>> bdy){
        this.con = con;
        this.heads = heads;
        this.bdy = bdy;
    }
    @Override
    public int getGroupCount() {
        return heads.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return bdy.get(heads.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return heads.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return bdy.get(heads.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String hd = (String) this.getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater li = (LayoutInflater) this.con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.listheading,null);
        }
        TextView txt = (TextView) convertView.findViewById(R.id.lhead);
        txt.setTypeface(null, Typeface.BOLD);
        txt.setText(hd);
        if(this._rls.get(groupPosition) > 0){
            txt.setTextColor(Color.parseColor("#E74C3C"));
//            txt.setBackgroundColor(Color.parseColor("#FFD9C7"));

        }else{
            txt.setTextColor(Color.parseColor("#16A085"));
//            txt.setBackgroundColor(Color.parseColor("#D4FFC7"));

        }
        return convertView ;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String bdy = (String) this.getChild(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater li = (LayoutInflater) this.con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.listbody,null);
        }
        TextView txt2 = (TextView) convertView.findViewById(R.id.lbody);
        Log.d("testingADAP",bdy);

        Log.d("RLS",this._rls.get(groupPosition).toString());
        Log.d("gposition",Integer.toString(groupPosition));
        if(this._rls.get(groupPosition) > 0){
            txt2.setBackgroundColor(Color.parseColor("#FFD9C7"));
        }else{
            txt2.setBackgroundColor(Color.parseColor("#CCFFCC"));
        }
        txt2.setText(Html.fromHtml(bdy));
        txt2.setMovementMethod(LinkMovementMethod.getInstance());
        return convertView ;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
