package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import course.examples.appdatphong.ChiTietKhachSanActivity;
import course.examples.appdatphong.R;
import model.sanpham;
import ultil.check_connection;

public class sanpham_adapter extends RecyclerView.Adapter<sanpham_adapter.ItemHolder> {

    Context context;
    ArrayList<sanpham> arr_sanpham;

    public sanpham_adapter(Context context, ArrayList<sanpham> arr_sanpham) {
        this.context = context;
        this.arr_sanpham = arr_sanpham;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_sanpham_moinhat,null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        sanpham sanpham = arr_sanpham.get(position);
        holder.tvtensp.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvgiasp.setText(decimalFormat.format(sanpham.getGiasanpham()) + " VND");
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(holder.imghinhsp);
    }

    @Override
    public int getItemCount() {
        return arr_sanpham.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView imghinhsp;
        public TextView tvtensp, tvgiasp;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imghinhsp = (ImageView) itemView.findViewById(R.id.imagesp);
            tvgiasp = (TextView) itemView.findViewById(R.id.textviewgiasp);
            tvtensp = (TextView) itemView.findViewById(R.id.textviewtensp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChiTietKhachSanActivity.class);
                    intent.putExtra("thongtinsp", arr_sanpham.get(getLayoutPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void updateArrayList(ArrayList<sanpham> newList) {
        arr_sanpham = new ArrayList<>();
        arr_sanpham.addAll(newList);
        notifyDataSetChanged();
    }
}
