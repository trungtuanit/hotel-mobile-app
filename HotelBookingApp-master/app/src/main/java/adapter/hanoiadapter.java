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

public class hanoiadapter extends BaseAdapter {

    Context context;
    ArrayList<sanpham> arr_hanoi;

    public hanoiadapter(Context context, ArrayList<sanpham> arr_hanoi) {
        this.context = context;
        this.arr_hanoi = arr_hanoi;
    }

    @Override
    public int getCount() {
        return arr_hanoi.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_hanoi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder
    {
        public TextView txtTenhanoi, txtGiahanoi, txtMota, txtDiachi;
        public ImageView imghanoi;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_hanoi,null);
            viewHolder.txtTenhanoi = (TextView) view.findViewById(R.id.tvtenhanoi);
            viewHolder.txtGiahanoi = (TextView) view.findViewById(R.id.tvgiahanoi);
            viewHolder.txtMota = (TextView) view.findViewById(R.id.tvmotahanoi);
            viewHolder.imghanoi = (ImageView) view.findViewById(R.id.imageviewhanoi);
            viewHolder.txtDiachi = (TextView) view.findViewById(R.id.tvdiachihanoi);
            view.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        sanpham sanpham = (sanpham) getItem(position);
        viewHolder.txtTenhanoi.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtGiahanoi.setText(decimalFormat.format(sanpham.getGiasanpham()) + " VND");
        viewHolder.txtMota.setMaxLines(2);
        viewHolder.txtMota.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtMota.setText(sanpham.getMota());
        viewHolder.txtDiachi.setText(sanpham.getDiachi());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imghanoi);
        return view;
    }

    public void updateArrayList(ArrayList<sanpham> newList) {
        arr_hanoi = new ArrayList<>();
        arr_hanoi.addAll(newList);
        notifyDataSetChanged();
    }
}
