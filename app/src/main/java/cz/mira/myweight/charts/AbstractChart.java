package cz.mira.myweight.charts;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cz.mira.myweight.R;
import cz.mira.myweight.rest.dto.WeightReportDTO;

public abstract class AbstractChart {

    private Context context;

    private View view;

    private List<WeightReportDTO> weightReport;

    public AbstractChart(Context context, View view, List<WeightReportDTO> weightReport) {
        this.context = context;
        this.view = view;
        this.weightReport = weightReport;
    }

    public final void createChart() {
        final LineChart lineChart = view.findViewById(R.id.lineChart);
        final ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 1; i < weightReport.size(); i++) {
            float x_points = TimeUnit.MILLISECONDS.toHours(weightReport.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            float y_points = getYAxisDataList(weightReport, i);
            entries.add(new Entry(x_points, y_points));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "BMI");
        lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        lineDataSet.setValueTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        setDataSetMode(lineDataSet);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MMM", Locale.ENGLISH);

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
        lineChart.getLegend().setEnabled(false);
    }

    abstract float getYAxisDataList(List<WeightReportDTO> weightReport, int index);

    abstract void setDataSetMode(LineDataSet lineDataSet);
}
