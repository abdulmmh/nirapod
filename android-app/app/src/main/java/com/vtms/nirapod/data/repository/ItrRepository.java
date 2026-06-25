package com.vtms.nirapod.data.repository;

import com.vtms.nirapod.data.api.ItrApi;
import com.vtms.nirapod.data.model.ItrRecord;
import java.util.List;
import retrofit2.Call;

public class ItrRepository {

    private final ItrApi itrApi;

    public ItrRepository(ItrApi itrApi) {
        this.itrApi = itrApi;
    }

    public Call<List<ItrRecord>> getMyReturns() {
        return itrApi.getMyReturns();
    }
}