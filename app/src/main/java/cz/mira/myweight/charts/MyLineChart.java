package cz.mira.myweight.charts;

import android.content.Context;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cz.mira.myweight.R;
import cz.mira.myweight.rest.dto.WeightReportDTO;

public class MyLineChart {

    LineChart lineChart;

    List<WeightReportDTO> weightReport;

    Context context;

    public MyLineChart(LineChart lineChart, List<WeightReportDTO> weightReport, Context context) {
        this.lineChart = lineChart;
        this.weightReport = weightReport;
        this.context = context;
        setUpChart();
    }

    private void setUpChart() {
        final ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 1; i < weightReport.size(); i++) {
            float x_points = Timestamp.valueOf(weightReport.get(i).getDate().toString()).getTime();
            float y_points = weightReport.get(i).getWeight().floatValue();
            entries.add(new Entry(x_points, y_points));

        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Weight");
        lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        lineDataSet.setValueTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);

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
