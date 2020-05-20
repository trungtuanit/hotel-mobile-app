package adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import course.examples.appdatphong.R;
import model.sanpham;

public class danangadapter extends BaseAdapter {

    Context context;
    ArrayList<sanpham> arr_danang;

    public danangadapter(Context context, ArrayList<sanpham> arr_danang) {
        this.context = context;
        this.arr_danang = arr_danang;
    }

    @Override
    public int getCount() {
        return arr_danang.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_danang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder
    {
        public TextView txtTendanang, txtGiadanang, txtMota, txtDiachi;
        public ImageView imgdanang;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_danang,null);
            viewHolder.txtTendanang = (TextView) view.findViewById(R.id.tvtendanang);
            viewHolder.txtGiadanang = (TextView) view.findViewById(R.id.tvgiadanang);
            viewHolder.txtMota = (TextView) view.findViewById(R.id.tvmotadanang);
            viewHolder.imgdanang = (ImageView) view.findViewById(R.id.imageviewdanang);
            viewHolder.txtDiachi = (TextView) view.findViewById(R.id.tvdiachidanang);
            view.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        sanpham sanpham = (sanpham) getItem(position);
        viewHolder.txtTendanang.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtGiadanang.setText(decimalFormat.format(sanpham.getGiasanpham()) + " VND");
        viewHolder.txtMota.setMaxLines(2);
        viewHolder.txtMota.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtMota.setText(sanpham.getMota());
        viewHolder.txtDiachi.setText(sanpham.getDiachi());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imgdanang);
        return view;
    }

    public void updateArrayList(ArrayList<sanpham> newList) {
        arr_danang = new ArrayList<>();
        arr_danang.addAll(newList);
        notifyDataSetChanged();
    }
}
