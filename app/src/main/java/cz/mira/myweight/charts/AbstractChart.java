package cz.mira.myweight.charts;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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

    private static final String TAG = "AbstractChart";

    private static final SimpleDateFormat chartDateFormat = new SimpleDateFormat("dd/MMM", Locale.ENGLISH);

    private static final SimpleDateFormat printDateFormat = new SimpleDateFormat("dd.MMMM yyyy", Locale.ENGLISH);

    private Context context;

    private View view;

    private List<WeightReportDTO> weightReport;

    private float selectedXValue;

    private float selectedYValue;

    String chartName;

    public AbstractChart(Context context, View view, List<WeightReportDTO> weightReport) {
        this.context = context;
        this.view = view;
        this.weightReport = weightReport;
    }

    public final void createLineChart() {
        final LineChart lineChart = view.findViewById(R.id.lineChart);
        final ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 1; i < weightReport.size(); i++) {
            float x_points = TimeUnit.MILLISECONDS.toHours(weightReport.get(i).getDate().atZone(
                    ZoneId.systemDefault()).toInstant().toEpochMilli());
            float y_points = getYAxisDataList(weightReport, i);
            entries.add(new Entry(x_points, y_points));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "BMI");
        lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        lineDataSet.setDrawValues(false);
//        lineDataSet.setValueTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        setDataSetMode(lineDataSet);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {


            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.HOURS.toMillis((long) value);
                return chartDateFormat.format(new Date(millis));
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

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                selectedXValue = e.getX();
                selectedYValue = e.getY();
                Log.d(TAG, "x: " + selectedXValue + " y: " + selectedYValue);
                TextView textView = view.findViewById(R.id.points_text_view);
                textView.setText("Date: " + printDateFormat.format(TimeUnit.HOURS.toMillis((long) selectedXValue)) + "\n" +
                        setPrintStringForXValue(selectedYValue));
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    abstract float getYAxisDataList(List<WeightReportDTO> weightReport, int index);

    abstract void setDataSetMode(LineDataSet lineDataSet);

    abstract String setPrintStringForXValue(float selectedYValue);
}
