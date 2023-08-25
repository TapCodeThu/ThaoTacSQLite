package com.example.ontapsqlite01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.MyViewHolder>{
    private IClickListener iClickListener;
    private Context context;
    private List<Model> modelList;

    public ModelAdapter(Context context, List<Model> modelList,IClickListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.iClickListener = listener;
    }
    public void updateList(List<Model> newModelList) {
        this.modelList.clear(); // Xóa danh sách hiện tại
        this.modelList.addAll(newModelList); // Thêm dữ liệu mới vào danh sách
        notifyDataSetChanged(); // Thông báo cho RecyclerView cần phải vẽ lại
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_layout,parent,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model model = modelList.get(position);
        holder.txtName.setText(model.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        holder.txtPrice.setText(new StringBuilder("Giá: ").append(decimalFormat.format(model.getPrice())).append(" VND"));
        holder.txtQuantity.setText(new StringBuilder("Số lương: ").append(model.getQuantity()));
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickListener.onUpdate(model);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickListener.onDelete(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName,txtPrice,txtQuantity;
        private ImageView btnUpdate,btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
