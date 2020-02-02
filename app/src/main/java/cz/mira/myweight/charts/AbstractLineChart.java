package cz.mira.myweight.charts;

import android.content.Context;
import android.graphics.Color;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cz.mira.myweight.R;
import cz.mira.myweight.rest.dto.WeightReportDTO;

public abstract class AbstractLineChart {

    private static final String TAG = "AbstractLineChart";

    private static final SimpleDateFormat chartDateFormat = new SimpleDateFormat("dd/MMM", Locale.ENGLISH);

    private static final SimpleDateFormat printDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

    private Context context;

    private View view;

    private List<WeightReportDTO> weightReport;

    private float selectedXValue;

    private float selectedYValue;

    private LineData lineData;

    public AbstractLineChart(Context context, View view, List<WeightReportDTO> weightReport) {
        this.context = context;
        this.view = view;
        this.weightReport = weightReport;
        createLineChart();
    }

    private final void createLineChart() {
        final LineChart lineChart = view.findViewById(R.id.lineChart);
        final LineDataSet lineDataSet = createLineDataSet(createEntries(weightReport), "",
                ContextCompat.getColor(context, R.color.colorPrimary));
        setUpLineDataSet(lineDataSet);

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
        setUpLineYAxis(yAxisLeft);

        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                selectedXValue = e.getX();
                selectedYValue = e.getY();
                TextView textView = view.findViewById(R.id.points_text_view);
                Log.d(TAG, e.getData().toString());
                textView.setText("Date: " + printDateFormat.format(TimeUnit.HOURS.toMillis((long) selectedXValue)) + "\n" +
                        setPrintStringForXValue(selectedYValue, e.getData()));
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    LineDataSet createLineDataSet(List<Entry> entries, String label, int lineColor) {
        final LineDataSet lineDataSet = new LineDataSet(entries, label);
        lineDataSet.setColor(lineColor);
        lineDataSet.setDrawValues(false);
        lineDataSet.setCircleColor(Color.BLUE);
        lineDataSet.setCircleHoleColor(Color.BLUE);
        lineDataSet.setLineWidth(2f);
        return lineDataSet;
    }

    String setPrintStringForXValue(float selectedYValue, Object data) {
        final ChartType chartType = (ChartType) data;
        return chartType.getChartName() + ": " + selectedYValue + " " + chartType.getUnit();
    }

    void addLineSetToLineData(ILineDataSet lineDataSet) {
        lineData.addDataSet(lineDataSet);
    }

    abstract List<Entry> createEntries(List<WeightReportDTO> weightReport);

    abstract float getXValue(List<WeightReportDTO> weightReport, int index);

    abstract void setUpLineDataSet(LineDataSet lineDataSet);

    abstract void setUpLineYAxis(YAxis axis);
}
