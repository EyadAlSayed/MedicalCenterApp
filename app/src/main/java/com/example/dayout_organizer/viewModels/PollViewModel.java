package com.example.dayout_organizer.viewModels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dayout_organizer.api.ApiClient;
import com.example.dayout_organizer.models.poll.PollData;
import com.example.dayout_organizer.models.poll.VoteData;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.dayout_organizer.config.AppConstants.getErrorMessage;

public class PollViewModel extends ViewModel {

    private final ApiClient apiClient = new ApiClient();

    private static PollViewModel instance;


    public static PollViewModel getINSTANCE(){
        if(instance == null)
            instance = new PollViewModel();
        return instance;
    }

    public MutableLiveData<Pair<PollData, String>> pollsMutableLiveData;
    public MutableLiveData<Pair<VoteData, String>> votesMutableLiveData;
    public MutableLiveData<Pair<PollData, String>> createPollMutableLiveData;

    public void getPolls(){
        pollsMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getPolls().enqueue(new Callback<PollData>() {
            @Override
            public void onResponse(Call<PollData> call, Response<PollData> response) {
                if(response.isSuccessful()){
                    pollsMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        pollsMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PollData> call, Throwable t) {
                pollsMutableLiveData.setValue(null);
            }
        });
    }

    public void getVotes(){
        votesMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getVotes().enqueue(new Callback<VoteData>() {
            @Override
            public void onResponse(Call<VoteData> call, Response<VoteData> response) {
                if(response.isSuccessful()){
                    votesMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        votesMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<VoteData> call, Throwable t) {
                votesMutableLiveData.setValue(null);
            }
        });
    }

    public void createPoll(PollData poll){
        createPollMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().createPoll(poll).enqueue(new Callback<PollData>() {
            @Override
            public void onResponse(Call<PollData> call, Response<PollData> response) {
                if(response.isSuccessful()){
                    createPollMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        createPollMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PollData> call, Throwable t) {
                createPollMutableLiveData.setValue(null);
            }
        });
    }
}
