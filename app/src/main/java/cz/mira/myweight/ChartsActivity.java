package cz.mira.myweight;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.mira.myweight.charts.ChartType;
import cz.mira.myweight.database.DatabaseClient;
import cz.mira.myweight.database.async.AsyncTaskResult;
import cz.mira.myweight.database.entity.WeightLastUpdate;
import cz.mira.myweight.database.entity.WeightReport;
import cz.mira.myweight.rest.WeightRestService;
import cz.mira.myweight.rest.dto.WeightReportDTO;
import cz.mira.myweight.services.GmailService;
import cz.mira.myweight.ui.main.SectionsPagerAdapter;
import cz.mira.myweight.ui.main.fragments.ChartFragment;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChartsActivity extends AppCompatActivity implements ChartFragment.OnFragmentInteractionListener {

    private static final String TAG = "ChartsActivity";

    static final String BASE_URL = "http://mira182.synology.me:8000/";

    private GmailService gmailService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                        LocalDateTime.parse(json.getAsJsonPrimitive().getAsString()))
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        WeightRestService weightReportService = retrofit.create(WeightRestService.class);

        Call<List<WeightReportDTO>> getWeightReportCall = weightReportService.getWeightReport();
//            Call<Boolean> doesNewEmailExistCall = weightReportService.doesNewEmailExist();
        getWeightReportCall.enqueue(new Callback<List<WeightReportDTO>>() {
            @Override
            public void onResponse(Call<List<WeightReportDTO>> call, Response<List<WeightReportDTO>> response) {
                if (response.isSuccessful()) {
                    sectionsPagerAdapter.removeAll();
                    sectionsPagerAdapter.setWeightReport(Lists.newArrayList(response.body()));
                    int i = 0;
                    for (ChartType chartType : ChartType.values()) {
                        sectionsPagerAdapter.addFragment(ChartFragment.newInstance(Lists.newArrayList(response.body()), chartType), i++);
                    }
                    sectionsPagerAdapter.notifyDataSetChanged();
                } else {
                    try {
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<WeightReportDTO>> call, Throwable t) {
                Log.e(TAG, "Failed to call : " + call, t);
            }
        });
//            doesNewEmailExistCall.enqueue(new Callback<Boolean>() {
//                @Override
//                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                    if (response.isSuccessful()) {
//                        final String existsString = response.body() ? "exists" : "does not exist";
//                        Log.i(TAG, "New email " + existsString);
//                    } else {
//                        Log.e(TAG, "Failed to check if new email exists. " + response.errorBody().toString());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Boolean> call, Throwable t) {
//                    Log.e(TAG, "Failed to call : " + call, t);
//                }
//            });

        // old solution
//        gmailService = new GmailService();
//        weightService = new WeightService(gmailService, db);
    }

    private void saveTanitaEmailInDb() {

        class SaveTask extends AsyncTask<Void, Void, AsyncTaskResult<Boolean>> {

            @Override
            protected AsyncTaskResult<Boolean> doInBackground(Void... voids) {
                final byte[] csvFileBytes;
                try {
                    csvFileBytes = gmailService.getAttachmentFromMailByQuery();
                } catch (IOException | GeneralSecurityException e) {
                    return new AsyncTaskResult<>(e);
                }

                if (csvFileBytes == null)
                    return new AsyncTaskResult<>(new IOException("Failed to get attachment from email."));

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .weightReportDAO()
                        .deleteAll();

                CSVParser csvParser;
                try {
                    csvParser = CSVFormat.DEFAULT
                            .withFirstRecordAsHeader()
                            .parse(new InputStreamReader(new ByteArrayInputStream(csvFileBytes)));
                } catch (IOException e) {
                    return new AsyncTaskResult<>(e);
                }

                for (CSVRecord record : csvParser) {
                    String dateString = record.get("Date");
                    String weight = record.get("Weight (kg)");
                    String bmi = record.get("BMI");
                    String bodyFat = record.get("Body Fat (%)");
                    String viscFat = record.get("Visc Fat");
                    String muscleMass = record.get("Muscle Mass (kg)");
                    String muscleQuality = record.get("Muscle Quality");
                    String boneMass = record.get("Bone Mass (kg)");
                    String bmr = record.get("BMR (kcal)");
                    String metabAge = record.get("Metab Age");
                    String bodyWatter = record.get("Body Water (%)");
                    String phyRating = record.get("Physique Rating");

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime date = LocalDateTime.parse(dateString, formatter);

                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .weightReportDAO()
                            .insert(WeightReport.builder()
                                    .boneMass(Double.valueOf(boneMass))
                                    .muscleMass(Double.valueOf(muscleMass))
                                    .bmi(Double.valueOf(bmi))
                                    .bmr(Double.valueOf(bmr))
                                    .bodyFat(Double.valueOf(bodyFat))
                                    .bodyWatter(Double.valueOf(bodyWatter))
                                    .metabolicAge(Double.valueOf(metabAge))
                                    .muscleQuality(Double.valueOf(muscleQuality))
                                    .physiqueRating(Double.valueOf(phyRating))
                                    .visceralFat(Double.valueOf(viscFat))
                                    .weight(Double.valueOf(weight))
                                    .date(date)
                                    .build());
                }

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .weightLastUpdateDao()
                        .insert(WeightLastUpdate.builder()
                                .lastUpdated(LocalDateTime.now())
                                .build());

                return new AsyncTaskResult<>(true);
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<Boolean> result) {
                finish();
                if (result.getError() != null ) {
                    Log.e(TAG, "Failed to save attachment.", result.getError());
                    Toast.makeText(getApplicationContext(), "Failed to save attachment.",
                            Toast.LENGTH_LONG).show();
                }  else if ( isCancelled()) {
                    // cancel handling here
                } else {
                    Boolean realResult = result.getResult();
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                }

            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    private void getWeightReport(Retrofit retrofit) {
        class GetWeightReportTask extends AsyncTask<Retrofit, Void, AsyncTaskResult<List<WeightReportDTO>>> {

            @Override
            protected AsyncTaskResult<List<WeightReportDTO>> doInBackground(Retrofit... retrofits) {
                // create an instance of the ApiService
                WeightRestService apiService = retrofits[0].create(WeightRestService.class);
                // make a request by calling the corresponding method
                Call<List<WeightReportDTO>> weightReportCall = apiService.getWeightReport();
                try {
                    Response<List<WeightReportDTO>> weightReportResponse = weightReportCall.execute();
                    Log.d(TAG, weightReportResponse.toString());
                    return new AsyncTaskResult<>(weightReportResponse.body());
                } catch (IOException e) {
                    return new AsyncTaskResult<>(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<List<WeightReportDTO>> result) {
                if (result.getError() != null) {
                    Log.e(TAG, "Failed to get weight report.", result.getError());
                    Toast.makeText(getApplicationContext(), "Failed to get weight report.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Weight report: " +
                            result.getResult(), Toast.LENGTH_LONG).show();
                }
            }
        }

        GetWeightReportTask task = new GetWeightReportTask();
        task.execute(retrofit);
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
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
