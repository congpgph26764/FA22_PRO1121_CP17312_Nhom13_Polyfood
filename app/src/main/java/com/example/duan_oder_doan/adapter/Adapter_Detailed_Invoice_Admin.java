package com.example.duan_oder_doan.adapter;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_oder_doan.GioHangUser;
import com.example.duan_oder_doan.OdersManageActivity;
import com.example.duan_oder_doan.R;
import com.example.duan_oder_doan.model.HoaDonChiTiet;
import com.example.duan_oder_doan.model.HoaDonChiTietAdmin;
import com.example.duan_oder_doan.view_holder.View_Holder_Detailed_Invoice_Admin;
import com.example.duan_oder_doan.view_holder.View_Holder_Detailed_Invoice_User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adapter_Detailed_Invoice_Admin extends RecyclerView.Adapter<View_Holder_Detailed_Invoice_Admin> {
    private List<HoaDonChiTietAdmin> hoaDonChiTietAdminList;

    public Adapter_Detailed_Invoice_Admin(List<HoaDonChiTietAdmin> hoaDonChiTietAdminList) {
        this.hoaDonChiTietAdminList = hoaDonChiTietAdminList;
    }

    @NonNull
    @Override
    public View_Holder_Detailed_Invoice_Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_detailed_invoice_admin, parent, false);

        View_Holder_Detailed_Invoice_Admin viewHolderDetailedInvoiceAdmin = new View_Holder_Detailed_Invoice_Admin(row);
        return viewHolderDetailedInvoiceAdmin;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Detailed_Invoice_Admin holder, int position) {
        HoaDonChiTietAdmin hoaDonChiTietAdmin = hoaDonChiTietAdminList.get(position);

        holder.tvDate.setText(hoaDonChiTietAdmin.getDate());
        holder.tvPrice.setText(hoaDonChiTietAdmin.getSum_Price());
        holder.line_item.setOnClickListener(v ->{
            final Dialog dialog = new Dialog(v.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.dialog_hodonchitiet);

            TextView tv_name = dialog.findViewById(R.id.tv_name);
            TextView tv_phone = dialog.findViewById(R.id.tv_phone);
            TextView tv_address = dialog.findViewById(R.id.tv_address);
            TextView tv_date = dialog.findViewById(R.id.tv_date);
            TextView tv_sum = dialog.findViewById(R.id.tv_sum);

            tv_name.setText("Name: "+ hoaDonChiTietAdmin.getName());
            tv_phone.setText("Phone: "+ hoaDonChiTietAdmin.getPhone());
            tv_address.setText("Address: "+ hoaDonChiTietAdmin.getAddress());
            tv_date.setText("Date: "+ hoaDonChiTietAdmin.getDate());
            tv_sum.setText("Sum: $ "+ hoaDonChiTietAdmin.getSum_Price());

            tv_phone.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:"+hoaDonChiTietAdmin.getPhone()));
                    dialog.getContext().startActivity(i);
                    return false;
                }
            });

            dialog.show();
        });

        holder.tvStatus.setText(hoaDonChiTietAdmin.getStatus());
        if (holder.tvStatus.getText().toString().equals("Confirm")) {
            holder.tvStatus.setOnClickListener(v ->{
                hoaDonChiTietAdminList.clear();
                notifyDataSetChanged();
                String status = "Doing";
                HoaDonChiTietAdmin hoaDonChiTietAdmin1 = new HoaDonChiTietAdmin(hoaDonChiTietAdmin.getId(), hoaDonChiTietAdmin.getDate(), hoaDonChiTietAdmin.getSum_Price(), hoaDonChiTietAdmin.getName(), hoaDonChiTietAdmin.getPhone(), hoaDonChiTietAdmin.getAddress(), status);
                FirebaseDatabase.getInstance().getReference("Detailed_Invoices")
                        .child(String.valueOf(hoaDonChiTietAdmin.getId()))
                        .setValue(hoaDonChiTietAdmin1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(v.getContext(), OdersManageActivity.class);
                                    v.getContext().startActivity(intent);
                                }
                            }
                        });

            });
        }
        if (holder.tvStatus.getText().toString().equals("Doing")) {
            hoaDonChiTietAdminList.clear();
            notifyDataSetChanged();
            holder.tvStatus.setVisibility(View.INVISIBLE);
        }
        if (holder.tvStatus.getText().toString().equals("Done")) {
            hoaDonChiTietAdminList.clear();
            notifyDataSetChanged();
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackgroundResource(R.drawable.border2);
        }

    }

    @Override
    public int getItemCount() {
        return hoaDonChiTietAdminList == null ? 0 : hoaDonChiTietAdminList.size();
    }
}