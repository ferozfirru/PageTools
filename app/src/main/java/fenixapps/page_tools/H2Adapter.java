package fenixapps.page_tools;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

    public class H2Adapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<H2Object> objects;

        private class ViewHolder {
            TextView txt_key;
            TextView txt_value;
        }

        public H2Adapter(Context context, ArrayList<H2Object> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }

        public H2Object getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.h2body, null);
                holder.txt_key = (TextView) convertView.findViewById(R.id.h2key);
                holder.txt_value = (TextView) convertView.findViewById(R.id.h2value);
                convertView.setTag(holder);
            } else {
                holder = (fenixapps.page_tools.H2Adapter.ViewHolder) convertView.getTag();
            }
            LinearLayout dnsll = (LinearLayout) convertView.findViewById(R.id.dnsll1);
            if(objects.get(position).getKey().equals("A"))
            {
                dnsll.setBackgroundColor(Color.parseColor("#FF5722"));
            }
            if(objects.get(position).getValue().equals("NS"))
            {
                dnsll.setBackgroundColor(Color.parseColor("#00695C"));
            }

            holder.txt_key.setText(objects.get(position).getKey());
            holder.txt_value.setText(objects.get(position).getValue());

            return convertView;
        }


}
