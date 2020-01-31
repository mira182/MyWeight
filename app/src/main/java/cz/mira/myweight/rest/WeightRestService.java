package cz.mira.myweight.rest;

import java.util.List;

import cz.mira.myweight.rest.dto.WeightReportDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WeightRestService {

        @GET("weight/getReport")
        Call<List<WeightReportDTO>> getWeightReport();

        @GET("weight/doesNewEmailExist")
        Call<Boolean> doesNewEmailExist();

        @GET("weight/updateReport")
        Call<Boolean> updateWeightReport();
}
