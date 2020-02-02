package cz.mira.myweight.charts;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.mira.myweight.rest.dto.WeightReportDTO;

public class BodyFatLineChart extends AbstractLineChart {

    public BodyFatLineChart(Context context, View view, List<WeightReportDTO> weightReport) {
        super(context, view, weightReport);
    }

    @Override
    List<Entry> createEntries(List<WeightReportDTO> weightReport) {
        final List<Entry> entries = new ArrayList<>();
        for (int i = 1; i < weightReport.size(); i++) {
            float x_points = TimeUnit.MILLISECONDS.toHours(weightReport.get(i).getDate().atZone(
                    ZoneId.systemDefault()).toInstant().toEpochMilli());
            float y_points = getXValue(weightReport, i);
            entries.add(new Entry(x_points, y_points, ChartType.BODY_FAT.getChartName()));
        }
        return entries;
    }

    @Override
    float getXValue(List<WeightReportDTO> weightReport, int index) {
        return weightReport.get(index).getBodyFat().floatValue();
    }

    @Override
    void setUpLineDataSet(LineDataSet lineDataSet) {
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }

    @Override
    void setUpLineYAxis(YAxis axis) {

    }
}
