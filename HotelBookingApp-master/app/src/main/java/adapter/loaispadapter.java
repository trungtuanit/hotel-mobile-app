package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import course.examples.appdatphong.R;
import model.loaisp;

public class loaispadapter extends BaseAdapter {
    ArrayList<loaisp> arraylist_loaisp;
    Context context;

    public loaispadapter(ArrayList<loaisp> arraylist_loaisp, Context context) {
        this.arraylist_loaisp = arraylist_loaisp;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arraylist_loaisp.size();
    }

    @Override
    public Object getItem(int i) {
        return arraylist_loaisp.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder
    {
        TextView textView_tenloaisp;
        ImageView imageView_loaisp;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_listview_loaisp,null);
            viewHolder.textView_tenloaisp = (TextView) view.findViewById(R.id.textview_loaisp);
            viewHolder.imageView_loaisp = (ImageView) view.findViewById(R.id.imageview_loaisp);
            view.setTag(viewHolder);

        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        loaisp loaisp = (loaisp) getItem(i);
        viewHolder.textView_tenloaisp.setText(loaisp.getTenloaisp());
        Picasso.with(context).load(loaisp.getHinhanhloaisp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imageView_loaisp);
        return view;
    }
}
