package com.sanapplications.mygarage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {
    private Spinner makeSpinner, modelSpinner;
    private List<String> makeList, modelList;
    private Button addButton;
    private ListView vehicleListView;

    private VehicleDatabaseHelper databaseHelper;
    private List<Vehicle> vehicleList;
    private ArrayAdapter<Vehicle> vehicleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        makeSpinner = findViewById(R.id.make_spinner);
        modelSpinner = findViewById(R.id.model_spinner);
        addButton = findViewById(R.id.add_button);
        vehicleListView = findViewById(R.id.vehicle_list);

        makeList = new ArrayList<>();
        makeList.add("Honda");
        makeList.add("Hyundai");
        makeList.add("Tata");

        modelList = new ArrayList<>();
        modelList.add("City");
        modelList.add("Brio");

        ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makeList);
        makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        makeSpinner.setAdapter(makeAdapter);

        ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelList);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(modelAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String make = makeSpinner.getSelectedItem().toString();
                String model = modelSpinner.getSelectedItem().toString();
                Vehicle vehicle = new Vehicle(make, model);
                long id = databaseHelper.addVehicle(vehicle);
                vehicle.setId(id);
                vehicleAdapter.add(vehicle);
                vehicleAdapter.notifyDataSetChanged();
            }
        });

        databaseHelper = new VehicleDatabaseHelper(this);
        vehicleList = databaseHelper.getAllVehicles();
        vehicleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehicleList);
        vehicleListView.setAdapter(vehicleAdapter);
        vehicleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vehicle vehicle = vehicleAdapter.getItem(position);
                Toast.makeText(DashBoardActivity.this, "Selected " + vehicle, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

