package cz.mira.myweight.charts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cz.mira.myweight.R;
import cz.mira.myweight.rest.dto.WeightReportDTO;

public class MyLineChartActivity extends AppCompatActivity {

    private static final String TAG = "MyLineChartActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart_acitivity);
        Intent intent = getIntent();
        List<WeightReportDTO> weightReport = (List<WeightReportDTO>) intent.getSerializableExtra("weightReport");
        LineChart lineChart = findViewById(R.id.lineChart);
        final ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 1; i < weightReport.size(); i++) {
            Log.d(TAG, weightReport.get(i).getDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")));
            float x_points =weightReport.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            float y_points = weightReport.get(i).getWeight().floatValue();
            entries.add(new Entry(x_points, y_points));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Weight");
        lineDataSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        lineDataSet.setValueTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setGranularity(1f);

        LineData data = new LineData(lineDataSet);
        lineChart.setData(data);
        lineChart.animateX(2500);
        lineChart.invalidate();
    }
}
