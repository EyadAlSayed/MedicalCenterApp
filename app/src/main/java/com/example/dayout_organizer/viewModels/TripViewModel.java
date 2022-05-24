package com.example.dayout_organizer.viewModels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dayout_organizer.api.ApiClient;
import com.example.dayout_organizer.models.trip.TripData;
import com.example.dayout_organizer.models.trip.TripType;
import com.example.dayout_organizer.models.trip.photo.TripPhotoModel;
import com.example.dayout_organizer.models.trip.TripDetailsModel;
import com.example.dayout_organizer.models.trip.TripModel;
import com.example.dayout_organizer.models.trip.create.CreateTripPhoto;
import com.example.dayout_organizer.models.trip.create.CreateTripPlace;
import com.example.dayout_organizer.models.trip.create.CreateTripType;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.dayout_organizer.api.ApiClient.BASE_URL;
import static com.example.dayout_organizer.config.AppConstants.getErrorMessage;

public class TripViewModel extends ViewModel {

    private static final String TAG = "TripViewModel";

    private final ApiClient apiClient = new ApiClient();

    private static TripViewModel instance;

    public static final String  TRIP_PHOTOS_URL = BASE_URL + "api/trip/photo/";



    public static TripViewModel getINSTANCE() {
        if (instance == null) {
            instance = new TripViewModel();
        }
        return instance;
    }

    public MutableLiveData<Pair<TripData, String>> createTripMutableLiveData;
    public MutableLiveData<Pair<List<TripType>, String>> tripTypeTripMutableLiveData;


    public MutableLiveData<Pair<TripModel, String>> upcomingTripsMutableLiveData;
    public MutableLiveData<Pair<TripModel, String>> activeTripsMutableLiveData;
    public MutableLiveData<Pair<TripModel, String>> historyTripsMutableLiveData;
    public MutableLiveData<Pair<TripPhotoModel, String>> tripPhotosMutableLiveData;


    public MutableLiveData<Pair<TripDetailsModel, String>> tripDetailsMutableLiveData;

    public void getTripType(){
        tripTypeTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getTripType().enqueue(new Callback<List<TripType>>() {
            @Override
            public void onResponse(Call<List<TripType>> call, Response<List<TripType>> response) {
                if (response.isSuccessful()) {
                    tripTypeTripMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        tripTypeTripMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TripType>> call, Throwable t) {
                tripTypeTripMutableLiveData.setValue(null);
            }
        });
    }

    public void createTrip(JsonObject jsonObject) {
        createTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().createTrip(jsonObject).enqueue(new Callback<TripData>() {
            @Override
            public void onResponse(Call<TripData> call, Response<TripData> response) {
                if (response.isSuccessful()) {
                    createTripMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        createTripMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripData> call, Throwable t) {
                createTripMutableLiveData.setValue(null);
            }
        });
    }

    public void getUpcomingTrips(){
        upcomingTripsMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getUpcomingTrips().enqueue(new Callback<TripModel>() {
            @Override
            public void onResponse(Call<TripModel> call, Response<TripModel> response) {
                if(response.isSuccessful()){
                    upcomingTripsMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        upcomingTripsMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripModel> call, Throwable t) {
                upcomingTripsMutableLiveData.setValue(null);
            }
        });
    }

    public void getActiveTrips(){
        activeTripsMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getActiveTrips().enqueue(new Callback<TripModel>() {
            @Override
            public void onResponse(Call<TripModel> call, Response<TripModel> response) {
                if(response.isSuccessful()){
                    activeTripsMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        activeTripsMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripModel> call, Throwable t) {
                activeTripsMutableLiveData.setValue(null);
            }
        });
    }

    public void getHistoryTrips(){
        historyTripsMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getHistoryTrips().enqueue(new Callback<TripModel>() {
            @Override
            public void onResponse(Call<TripModel> call, Response<TripModel> response) {
                if(response.isSuccessful()){
                    historyTripsMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else{
                    try {
                        historyTripsMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripModel> call, Throwable t) {
                historyTripsMutableLiveData.setValue(null);
            }
        });
    }


    public void createTripPhoto(CreateTripPhoto createTripPhoto) {
        createTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().createTripPhoto(createTripPhoto).enqueue(new Callback<TripData>() {
            @Override
            public void onResponse(Call<TripData> call, Response<TripData> response) {
                if (response.isSuccessful()) {
                    createTripMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        createTripMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripData> call, Throwable t) {
                createTripMutableLiveData.setValue(null);
            }
        });
    }


    public void createTripPLace(CreateTripPlace createTripPlace) {
        createTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().createTripPlace(createTripPlace).enqueue(new Callback<TripData>() {
            @Override
            public void onResponse(Call<TripData> call, Response<TripData> response) {
                if (response.isSuccessful()) {
                    createTripMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        createTripMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripData> call, Throwable t) {
                createTripMutableLiveData.setValue(null);
            }
        });
    }

    public void createTripType(CreateTripType createTripType) {
        createTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().createTripType(createTripType).enqueue(new Callback<TripData>() {
            @Override
            public void onResponse(Call<TripData> call, Response<TripData> response) {
                if (response.isSuccessful()) {
                    createTripMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        createTripMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripData> call, Throwable t) {
                createTripMutableLiveData.setValue(null);
            }
        });
    }


    public void getTripPhotos(){
        tripPhotosMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getTripPhotos().enqueue(new Callback<TripPhotoModel>() {
            @Override
            public void onResponse(Call<TripPhotoModel> call, Response<TripPhotoModel> response) {
                if (response.isSuccessful()){
                    tripPhotosMutableLiveData.setValue(new Pair<>(response.body(),null));
                }

            }

            @Override
            public void onFailure(Call<TripPhotoModel> call, Throwable t) {
                tripPhotosMutableLiveData.setValue(null);
            }
        });
    }

    public void editTrip(JsonObject editTrip){
        createTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().editTrip(editTrip).enqueue(new Callback<TripData>() {
            @Override
            public void onResponse(Call<TripData> call, Response<TripData> response) {
                if (response.isSuccessful()){
                    createTripMutableLiveData.setValue(new Pair<>(response.body(),null));
                }
                else {
                    try {
                        createTripMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripData> call, Throwable t) {
                createTripMutableLiveData.setValue(null);
            }
        });
    }

    public void editTripPhotos(CreateTripPhoto createTripPhoto){
        createTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().editTripPhotos(createTripPhoto).enqueue(new Callback<TripData>() {
            @Override
            public void onResponse(Call<TripData> call, Response<TripData> response) {
                if (response.isSuccessful()){
                    createTripMutableLiveData.setValue(new Pair<>(response.body(),null));
                }
            }

            @Override
            public void onFailure(Call<TripData> call, Throwable t) {
                createTripMutableLiveData.setValue(null);
            }
        });
    }

    public void editTripTypes(CreateTripType createTripType){
        createTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().editTripTypes(createTripType).enqueue(new Callback<TripData>() {
            @Override
            public void onResponse(Call<TripData> call, Response<TripData> response) {
                if (response.isSuccessful()){
                    createTripMutableLiveData.setValue(new Pair<>(response.body(),null));
                }
            }

            @Override
            public void onFailure(Call<TripData> call, Throwable t) {
                createTripMutableLiveData.setValue(null);
            }
        });
    }

    public void editTripPlaces(CreateTripPlace createTripPlace){
        createTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().editTripPlaces(createTripPlace).enqueue(new Callback<TripData>() {
            @Override
            public void onResponse(Call<TripData> call, Response<TripData> response) {
                if (response.isSuccessful()){
                    createTripMutableLiveData.setValue(new Pair<>(response.body(),null));
                }
            }

            @Override
            public void onFailure(Call<TripData> call, Throwable t) {
                createTripMutableLiveData.setValue(null);
            }
        });
    }

    public void getTripDetails(int id){
        tripDetailsMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getTripDetails(id).enqueue(new Callback<TripDetailsModel>() {
            @Override
            public void onResponse(Call<TripDetailsModel> call, Response<TripDetailsModel> response) {
                if(response.isSuccessful()){
                    tripDetailsMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        tripDetailsMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripDetailsModel> call, Throwable t) {
                tripDetailsMutableLiveData.setValue(null);
            }
        });
    }

}