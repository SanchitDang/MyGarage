package com.sanapplications.mygarage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanapplications.mygarage.Helper.VehicleDatabaseHelper;
import com.sanapplications.mygarage.R;
import com.sanapplications.mygarage.Model.VehicleModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    private VehicleDatabaseHelper databaseHelper;
    private List<VehicleModel> vehicleModelList;
    private Context context;


    public VehicleAdapter(List<VehicleModel> vehicleModelList, Context context) {
        this.vehicleModelList = vehicleModelList;
        this.context = context;
        databaseHelper = new VehicleDatabaseHelper(context);
    }

    public VehicleAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehiclecard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VehicleModel vehicleModel = vehicleModelList.get(position);
        holder.vehicleName.setText(vehicleModel.getMake() + " " + vehicleModel.getModel());
        holder.vehicleYear.setText(vehicleModel.getModel());

        // Load the vehicle image using a third-party library like Glide or Picasso
        //Glide.with(context).load(vehicle.getImageUrl()).into(holder.vehicleImage);

        // Set click listeners for buttons
        holder.editButton.setOnClickListener(v -> {
            // Handle edit button click

        });

        holder.deleteButton.setOnClickListener(v -> {
            // Handle delete button click

            VehicleModel vehicleModell = vehicleModelList.get(position);
            if (vehicleModell != null) {
                long id = vehicleModel.getId();
                databaseHelper.removeVehicle(vehicleModell);
                vehicleModelList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, vehicleModelList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleModelList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView vehicleName;
        public TextView vehicleYear;
        public ImageView vehicleImage;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            vehicleName = itemView.findViewById(R.id.vehicle_make);
            vehicleYear = itemView.findViewById(R.id.vehicle_model);
            vehicleImage = itemView.findViewById(R.id.vehicle_image);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }


    public void setContext(Context context) {
        this.context = context;
    }


}
