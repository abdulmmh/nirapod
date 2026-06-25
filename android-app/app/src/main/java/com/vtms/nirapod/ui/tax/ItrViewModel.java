package com.vtms.nirapod.ui.tax;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.ItrRecord;
import com.vtms.nirapod.data.repository.ItrRepository;
import com.vtms.nirapod.util.NetworkResult;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class ItrViewModel extends AndroidViewModel {

    private final ItrRepository repository;
    private final MutableLiveData<NetworkResult<List<ItrRecord>>> itrResult
            = new MutableLiveData<>();

    public ItrViewModel(@NonNull Application app) {
        super(app);
        TokenManager tm = new TokenManager(app);
        repository = new ItrRepository(RetrofitClient.itrApi(tm));
    }

    public LiveData<NetworkResult<List<ItrRecord>>> getItrResult() {
        return itrResult;
    }

    public void loadMyReturns() {
        itrResult.setValue(NetworkResult.loading());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<ItrRecord>> response =
                        repository.getMyReturns().execute();
                if (response.isSuccessful() && response.body() != null) {
                    itrResult.postValue(NetworkResult.success(response.body()));
                } else {
                    itrResult.postValue(NetworkResult.error("Failed to load returns"));
                }
            } catch (Exception e) {
                itrResult.postValue(NetworkResult.error("Cannot connect to server"));
            }
        });
    }
}