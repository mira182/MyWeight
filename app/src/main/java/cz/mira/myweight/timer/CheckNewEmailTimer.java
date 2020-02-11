package cz.mira.myweight.timer;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import cz.mira.myweight.rest.RestUtils;
import cz.mira.myweight.rest.weight.WeightRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckNewEmailTimer {

    private static final String TAG = "CheckNewEmailTimer";

    private static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds

    private Consumer<Boolean> callback;

    public CheckNewEmailTimer(Consumer<Boolean> callback) {
        Timer mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
        this.callback = callback;
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            final WeightRestService weightRestService = RestUtils.getWeightRestService();
            Call<Boolean> doesNewEmailExistCall = weightRestService.doesNewEmailExist();
            doesNewEmailExistCall.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        final String existsString = response.body() ? "exists" : "does not exist";
                        callback.accept(response.body() ? true : false);
                        Log.i(TAG, "New email " + existsString);
                    } else {
                        Log.e(TAG, "Failed to check if new email exists. " + response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e(TAG, "Failed to call : " + call, t);
                }
            });
        }
    }
}
