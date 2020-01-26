package cz.mira.myweight.rest;

import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.mira.myweight.charts.MyLineChart;
import cz.mira.myweight.rest.dto.WeightReportDTO;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeightReportController implements Callback<List<WeightReportDTO>> {

    static final String BASE_URL = "http://mira182.synology.me:8000/";

    private static final String TAG = "WeightReportController";

    private List<WeightReportDTO> weightReport;

    private Context context;

    private LineChart lineChart;

    public WeightReportController(Context context, LineChart lineChart) {
        this.context = context;
        this.lineChart = lineChart;
    }

    public void start() {
        final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                        LocalDateTime.parse(json.getAsJsonPrimitive().getAsString()))
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        WeightRestService weightReportAPI = retrofit.create(WeightRestService.class);
        Call<List<WeightReportDTO>> call = weightReportAPI.getWeightReport();
        call.enqueue(this);
    }

    public List<WeightReportDTO> getWeightReport() {
        return weightReport;
    }

    @Override
    public void onResponse(Call<List<WeightReportDTO>> call, Response<List<WeightReportDTO>> response) {
        if (response.isSuccessful()) {
            weightReport = response.body();
            MyLineChart myLineChart = new MyLineChart(lineChart, weightReport, context);
        } else {
            Log.e(TAG, response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(Call<List<WeightReportDTO>> call, Throwable t) {
        Log.e(TAG, "Failed to call : " + call, t);
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.addInterceptor(chain -> {
                Request originalRequest = chain.request();

                Request.Builder b = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic("mira", "macos"));

                Request newRequest = b.build();
                return chain.proceed(newRequest);
            });
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
