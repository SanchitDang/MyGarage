package com.sanapplications.mygarage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.annotations.SerializedName;
import com.sanapplications.mygarage.API.MakeApi;
import com.sanapplications.mygarage.API.ModelApi;
import com.sanapplications.mygarage.Model.MakeResponse;
import com.sanapplications.mygarage.Model.MakeResult;
import com.sanapplications.mygarage.Model.ModelResponse;
import com.sanapplications.mygarage.Model.ModelResult;
import com.sanapplications.mygarage.R;
import com.sanapplications.mygarage.Model.VehicleModel;
import com.sanapplications.mygarage.Adapter.VehicleAdapter;
import com.sanapplications.mygarage.Helper.VehicleDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class DashboardActivity extends AppCompatActivity {
    private Spinner makeSpinner, modelSpinner;
    private List<String> makeList, modelList;
    private Button addButton;
    private RecyclerView vehicleListView;

    private VehicleDatabaseHelper databaseHelper;
    private List<VehicleModel> vehicleModelList;
    private ArrayAdapter<VehicleModel> vehicleAdapterdapter;

    private ModelApi modelApi;
    private RecyclerView recyclerView;
    private VehicleAdapter vehicleAdapter;

    private String selectedCarModelId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        recyclerView = findViewById(R.id.vehicle_list);


        makeSpinner = findViewById(R.id.make_spinner);
        modelSpinner = findViewById(R.id.model_spinner);
        addButton = findViewById(R.id.add_button);
        vehicleListView = findViewById(R.id.vehicle_list);

        databaseHelper = new VehicleDatabaseHelper(this);
        vehicleModelList = databaseHelper.getAllVehicles();

        VehicleAdapter vehicleAdapter = new VehicleAdapter(vehicleModelList, this);
        recyclerView.setAdapter(vehicleAdapter);


        // STATIC IMPLEMENTATION OF SPINNER (DROP BOX)
//        makeList = new ArrayList<>();
//        makeList.add("Honda");
//        makeList.add("Hyundai");
//        makeList.add("Tata");
//
//        modelList = new ArrayList<>();
//        modelList.add("City");
//        modelList.add("Brio");
//
//
//        ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makeList);
//        makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        makeSpinner.setAdapter(makeAdapter);
//
//        ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelList);
//        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        modelSpinner.setAdapter(modelAdapter);



        // DYNAMIC FROM API

        // Create Retrofit instance


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vpic.nhtsa.dot.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        // Create API service
        MakeApi makeApi = retrofit.create(MakeApi.class);

        // Make API call to get makes data
        Call<MakeResponse> call = makeApi.getMakes();
        call.enqueue(new Callback<MakeResponse>() {
            @Override
            public void onResponse(Call<MakeResponse> call, Response<MakeResponse> response) {
                if (response.isSuccessful()) {
                    List<MakeResult> results = response.body().Results;
                    Log.d("API: " , String.valueOf(response.body().Results));
                    List<String> makeList = new ArrayList<>();
                    for (MakeResult result : results) {
                        makeList.add(result.Make_Name+'_'+result.makeId);
                    }
                    // Set adapter for make dropdown
                    ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(DashboardActivity.this, android.R.layout.simple_spinner_dropdown_item, makeList);
                    makeSpinner.setAdapter(makeAdapter);
                } else {
                    Log.d("API call failed: " , String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<MakeResponse> call, Throwable t) {
                Log.d("API call failed: ", t.getMessage());
            }
        });




        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCarModelId = parent.getItemAtPosition(position).toString();
                Log.e("Selected value: ", getStringPartAfterUnderscore(selectedCarModelId));

                // Create API service
                ModelApi modelApi = retrofit.create(ModelApi.class);

                // Make API call to get makes data
                Call<ModelResponse> calll = modelApi.getModels(getStringPartAfterUnderscore(selectedCarModelId));
                calll.enqueue(new Callback<ModelResponse>() {
                    @Override
                    public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                        if (response.isSuccessful()) {
                            List<ModelResult> results = response.body().Results;
                            Log.d("3wqdf2w3fd","w23fd23f32");
                            Log.d("API: " , String.valueOf(response.body().Results));
                            List<String> modelList = new ArrayList<>();
                            for (ModelResult result : results) {
                                modelList.add(result.Model_Name);
                            }
                            // Set adapter for make dropdown
                            ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(DashboardActivity.this, android.R.layout.simple_spinner_dropdown_item, modelList);
                            modelSpinner.setAdapter(modelAdapter);
                        } else {
                            Log.d("API call failed: " , String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelResponse> call, Throwable t) {
                        Log.d("API call failed: ", t.getMessage());
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String make = makeSpinner.getSelectedItem().toString();
                String model = modelSpinner.getSelectedItem().toString();
                VehicleModel vehicleModel = new VehicleModel(make, model);
                long id = databaseHelper.addVehicle(vehicleModel);
                vehicleModel.setId(id);

                vehicleModelList.add(vehicleModel);
                vehicleAdapter.notifyDataSetChanged();

            }
        });




    }


    public String getStringPartAfterUnderscore(String input) {
        int underscoreIndex = input.indexOf('_');
        if (underscoreIndex >= 0 && underscoreIndex < input.length() - 1) {
            return input.substring(underscoreIndex + 1);
        }
        return ""; // Return an empty string if no underscore is found or it is at the end of the input.
    }









    public class RetrofitClientInstance {

        private static Retrofit retrofit;
        private static final String BASE_URL = "https://vpic.nhtsa.dot.gov";

        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new retrofit2.Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }



}

