package fenixapps.page_tools;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fenix on 31/3/17.
 */
public class DnsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<DnsObject> objects;

    private class ViewHolder {
        TextView txt_type;
        TextView txt_country;
        TextView txt_ttl;
        TextView txt_record;
    }

    public DnsAdapter(Context context, ArrayList<DnsObject> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public int getCount() {
        return objects.size();
    }

    public DnsObject getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.dnsbody, null);
            holder.txt_type = (TextView) convertView.findViewById(R.id.dnstype);
            holder.txt_country = (TextView) convertView.findViewById(R.id.dnscountry);
            holder.txt_ttl = (TextView) convertView.findViewById(R.id.dnsttl);
            holder.txt_record = (TextView) convertView.findViewById(R.id.dnsrecord);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LinearLayout dnsll = (LinearLayout) convertView.findViewById(R.id.dnsll1);
        if(objects.get(position).getType().equals("A"))
        {
            dnsll.setBackgroundColor(Color.parseColor("#FF5722"));
        }
        if(objects.get(position).getType().equals("NS"))
        {
            dnsll.setBackgroundColor(Color.parseColor("#00695C"));
        }
        if(objects.get(position).getType().equals("SOA"))
        {
            dnsll.setBackgroundColor(Color.parseColor("#00838F"));
        }
        if(objects.get(position).getType().equals("MX"))
        {
            dnsll.setBackgroundColor(Color.parseColor("#880E4F"));
        }
        if(objects.get(position).getType().equals("TXT"))
        {
            dnsll.setBackgroundColor(Color.parseColor("#4A148C"));
        }
        holder.txt_type.setText(objects.get(position).getType());
        holder.txt_country.setText(objects.get(position).getCountry());
        holder.txt_ttl.setText(objects.get(position).getTtl());
        holder.txt_record.setText(objects.get(position).getRecord());
        return convertView;
    }
}
