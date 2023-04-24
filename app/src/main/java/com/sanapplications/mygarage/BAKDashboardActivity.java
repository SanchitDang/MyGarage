package com.sanapplications.mygarage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class BAKDashboardActivity extends AppCompatActivity {
    private Spinner makeSpinner, modelSpinner;
    private List<String> makeList, modelList;
    private Button addButton;
    private ListView vehicleListView;

    private VehicleDatabaseHelper databaseHelper;
    private List<Vehicle> vehicleList;
    private ArrayAdapter<Vehicle> vehicleAdapter;

    private ModelApi modelApi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


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
                        makeList.add(result.Make_Name);
                    }
                    // Set adapter for make dropdown
                    ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(BAKDashboardActivity.this, android.R.layout.simple_spinner_dropdown_item, makeList);
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

        modelApi = RetrofitClientInstance.getRetrofitInstance().create(ModelApi.class);


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
              //  Vehicle vehicle = new Vehicle(make, model);
              //  long id = databaseHelper.addVehicle(vehicle);
              //  vehicle.setId(id);
              //  vehicleAdapter.add(vehicle);
                vehicleAdapter.notifyDataSetChanged();
            }
        });

        databaseHelper = new VehicleDatabaseHelper(this);
        vehicleList = databaseHelper.getAllVehicles();
        vehicleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehicleList);
        vehicleListView.setAdapter(vehicleAdapter);




        //vehicleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        vehicleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Vehicle vehicle = vehicleAdapter.getItem(position);
//                Toast.makeText(DashboardActivity.this, "Selected " + vehicle, Toast.LENGTH_SHORT).show();
//            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("we3", "aaaaaaaaaaaaaaaappppiiii");


                        // int makeId = makeList.get(position).getMakeId();
                        //int makeId = 10908;
                        // Make the API request to get the car models for the selected make
                        //Call<ModelResponse> call = modelApi.getModels(makeId);
                        Call<ModelResponse> call = modelApi.getModels();
                        call.enqueue(new Callback<ModelResponse>() {
                            @Override
                            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                                if (response.isSuccessful()) {
                                    modelList.clear();
                                    ModelResponse modelResponse = response.body();

                                    List<ModelResult> models = modelResponse.results;

                                    Log.d("we3", String.valueOf(modelResponse));
                                    Log.d("we3", String.valueOf(modelResponse.message));

//                                    for (Model model : models) {
//                                        modelList.add(model.getCarName());
//                                    }
//                                    modelAdapter.notifyDataSetChanged();

                                    if (models != null && models.size() > 0) {
                                        ModelResult model = models.get(0);
                                        modelList.add(model.getModel_Name());
                                    } else {
                                        Toast.makeText(BAKDashboardActivity.this, "No car models found", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(BAKDashboardActivity.this, "Unable to get car models", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ModelResponse> call, Throwable t) {
                                Toast.makeText(BAKDashboardActivity.this, "Failed to get car models: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


            }

        });
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
        private int count;
        private String message;
        private String searchCriteria;
        private List<ModelResult> results;

        public int getCount() {
            return count;
        }

        public String getMessage() {
            return message;
        }

        public String getSearchCriteria() {
            return searchCriteria;
        }

        public List<ModelResult> getResults() {
            return results;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setSearchCriteria(String searchCriteria) {
            this.searchCriteria = searchCriteria;
        }

        public void setResults(List<ModelResult> results) {
            this.results = results;
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
        @GET("api/vehicles/GetModelsForMakeId/10005?format=json")
        Call<ModelResponse> getModels();
    }

    public class RetrofitClientInstance {

        private static Retrofit retrofit;
        private static final String BASE_URL = "https://vpic.nhtsa.dot.gov";

        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }



}

