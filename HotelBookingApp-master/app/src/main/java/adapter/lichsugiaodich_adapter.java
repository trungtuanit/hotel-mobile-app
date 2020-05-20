package adapter;

import android.content.Context;
import android.media.Image;
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
import model.chitietdatphong;

public class lichsugiaodich_adapter extends BaseAdapter {
    Context context;
    ArrayList<chitietdatphong> arr_lichsugiaodich;

    public lichsugiaodich_adapter(Context context, ArrayList<chitietdatphong> arr_lichsugiaodich) {
        this.context = context;
        this.arr_lichsugiaodich = arr_lichsugiaodich;
    }

    @Override
    public int getCount() {
        return arr_lichsugiaodich.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_lichsugiaodich.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public TextView tvTenKSLichsu, tvGiaLichsu, tvTenPhongLichsu, tvSLPhongLichsu;
        public ImageView imgLichsu;
        public ImageView imgRemove;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_lichsu, null);
            viewHolder.imgLichsu = (ImageView) convertView.findViewById(R.id.img_lichsu);
            viewHolder.imgRemove = (ImageView) convertView.findViewById(R.id.img_remove);
            viewHolder.tvTenKSLichsu = (TextView) convertView.findViewById(R.id.tv_ten_khachsan_lichsu);
            viewHolder.tvTenPhongLichsu = (TextView) convertView.findViewById(R.id.tv_ten_phong_lichsu);
            viewHolder.tvGiaLichsu = (TextView) convertView.findViewById(R.id.tv_gia_lichsu);
            viewHolder.tvSLPhongLichsu = (TextView) convertView.findViewById(R.id.tv_slphong_lichsu);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        chitietdatphong chitietdatphong = (model.chitietdatphong) getItem(position);
        viewHolder.tvTenKSLichsu.setText(chitietdatphong.getTenkhachsan());
        viewHolder.tvTenPhongLichsu.setText(chitietdatphong.getTenphong());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvGiaLichsu.setText(decimalFormat.format(chitietdatphong.getGiaphong()) + " VND");
        viewHolder.tvSLPhongLichsu.setText("Số lượng phòng: " + chitietdatphong.getSlphong());
        Picasso.with(context).load(chitietdatphong.getHinhanh())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imgLichsu);
        if(chitietdatphong.getTrangthai() == 1){
            viewHolder.imgRemove.setBackgroundResource(R.drawable.remove_item);
        }
        return convertView;
    }

    public void updateArrayList(ArrayList<chitietdatphong> newList) {
        arr_lichsugiaodich = new ArrayList<>();
        arr_lichsugiaodich.addAll(newList);
        notifyDataSetChanged();
    }
}
