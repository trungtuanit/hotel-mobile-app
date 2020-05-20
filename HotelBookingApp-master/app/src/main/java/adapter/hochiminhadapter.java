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

public class hochiminhadapter extends BaseAdapter {

    Context context;
    ArrayList<sanpham> arr_hochiminh;

    public hochiminhadapter(Context context, ArrayList<sanpham> arr_hochiminh) {
        this.context = context;
        this.arr_hochiminh = arr_hochiminh;
    }

    @Override
    public int getCount() {
        return arr_hochiminh.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_hochiminh.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder
    {
        public TextView txtTenhochiminh, txtGiahochiminh, txtMota, txtDiachi;
        public ImageView imghochiminh;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_hochiminh,null);
            viewHolder.txtTenhochiminh = (TextView) view.findViewById(R.id.tvtenhochiminh);
            viewHolder.txtGiahochiminh = (TextView) view.findViewById(R.id.tvgiahochiminh);
            viewHolder.txtMota = (TextView) view.findViewById(R.id.tvmotahochiminh);
            viewHolder.imghochiminh = (ImageView) view.findViewById(R.id.imageviewhochiminh);
            viewHolder.txtDiachi = (TextView) view.findViewById(R.id.tvdiachihochiminh);
            view.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        sanpham sanpham = (sanpham) getItem(position);
        viewHolder.txtTenhochiminh.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtGiahochiminh.setText(decimalFormat.format(sanpham.getGiasanpham()) + " VND");
        viewHolder.txtMota.setMaxLines(2);
        viewHolder.txtMota.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtMota.setText(sanpham.getMota());
        viewHolder.txtDiachi.setText(sanpham.getDiachi());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imghochiminh);
        return view;
    }

    public void updateArrayList(ArrayList<sanpham> newList) {
        arr_hochiminh = new ArrayList<>();
        arr_hochiminh.addAll(newList);
        notifyDataSetChanged();
    }
}
