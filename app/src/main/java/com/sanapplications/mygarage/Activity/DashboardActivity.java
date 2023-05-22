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
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.sanapplications.mygarage.R;
import com.sanapplications.mygarage.Model.Vehicle;
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
    private List<Vehicle> vehicleList;
    private ArrayAdapter<Vehicle> vehicleAdapterdapter;

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
                Vehicle vehicle = new Vehicle(make, model);
                long id = databaseHelper.addVehicle(vehicle);
                vehicle.setId(id);

                //vehicleAdapter.add(vehicle);
                //vehicleAdapter.notifyDataSetChanged();

            }
        });



        databaseHelper = new VehicleDatabaseHelper(this);
        vehicleList = databaseHelper.getAllVehicles();


        // Recycler View
        VehicleAdapter vehicleAdapter = new VehicleAdapter(vehicleList, this);
        recyclerView.setAdapter(vehicleAdapter);

    }


    public String getStringPartAfterUnderscore(String input) {
        int underscoreIndex = input.indexOf('_');
        if (underscoreIndex >= 0 && underscoreIndex < input.length() - 1) {
            return input.substring(underscoreIndex + 1);
        }
        return ""; // Return an empty string if no underscore is found or it is at the end of the input.
    }


    public class MakeResponse {
        private int Count;
        private String Message;
        private List<MakeResult> Results;

        // Getters and setters
        private int count;
        private String message;
        private Object searchCriteria;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getSearchCriteria() {
            return searchCriteria;
        }

        public void setSearchCriteria(Object searchCriteria) {
            this.searchCriteria = searchCriteria;
        }

    }

    public class MakeResult {
//        private int Make_ID;
        private String Make_Name;
//
//        // Getters and setters

        @SerializedName("Make_ID")
        private int makeId;
        @SerializedName("Make_Name")
        //private String makeName;

        public int getMakeId() {
            return makeId;
        }

        public String getMakeName() {
            //return makeName;
            return Make_Name;
        }
    }

    public interface MakeApi {
        @GET("/api/vehicles/getallmakes?format=json")
        Call<MakeResponse> getMakes();

    }

    public class ModelResponse {
        private int Count;
        private String Message;
        private String SearchCriteria;
        private List<ModelResult> Results;

        public int getCount() {
            return Count;
        }

        public String getMessage() {
            return Message;
        }

        public String getSearchCriteria() {
            return SearchCriteria;
        }

        public List<ModelResult> getResults() {
            return Results;
        }

        public void setCount(int count) {
            this.Count = count;
        }

        public void setMessage(String message) {
            this.Message = message;
        }

        public void setSearchCriteria(String searchCriteria) {
            this.SearchCriteria = searchCriteria;
        }

        public void setResults(List<ModelResult> results) {
            this.Results = results;
        }
    }

    public class ModelResult {
    private int Make_ID;
    private String Make_Name;
    private int Model_ID;
    private String Model_Name;

    public int getMake_ID() {
        return Make_ID;
    }

    public String getMake_Name() {
        return Make_Name;
    }

    public int getModel_ID() {
        return Model_ID;
    }

    public String getModel_Name() {
        return Model_Name;
    }

    public void setMake_ID(int make_ID) {
        Make_ID = make_ID;
    }

    public void setMake_Name(String make_Name) {
        Make_Name = make_Name;
    }

    public void setModel_ID(int model_ID) {
        Model_ID = model_ID;
    }

    public void setModel_Name(String model_Name) {
        Model_Name = model_Name;
    }
}

    public interface ModelApi {
//        @GET("api/vehicles/GetModelsForMakeId/10005?format=json")
//        Call<ModelResponse> getModels();
@GET("api/vehicles/GetModelsForMakeId/{id}?format=json")
Call<ModelResponse> getModels(@Path("id") String id);
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

