package cz.mira.myweight.charts;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.mira.myweight.rest.dto.WeightReportDTO;

public class MultiLineChart extends AbstractLineChart {

    public MultiLineChart(Context context, View view, List<WeightReportDTO> weightReport) {
        super(context, view, weightReport);
        setUpLineData(weightReport);
    }

    private void setUpLineData(List<WeightReportDTO> weightReport) {
        final ArrayList<Entry> bodyFatEntries = new ArrayList<>();
        final ArrayList<Entry> muscleMassEntries = new ArrayList<>();
        final ArrayList<Entry> muscleQualityEntries = new ArrayList<>();
        final ArrayList<Entry> bodyWatterEntries = new ArrayList<>();
        for (int i = 1; i < weightReport.size(); i++) {
            float x_points = TimeUnit.MILLISECONDS.toHours(weightReport.get(i).getDate().atZone(
                    ZoneId.systemDefault()).toInstant().toEpochMilli());
            bodyFatEntries.add(new Entry(x_points, weightReport.get(i).getBodyFat().floatValue(), ChartType.BODY_FAT));
            bodyWatterEntries.add(new Entry(x_points, weightReport.get(i).getBodyWatter().floatValue(), ChartType.BODY_WATTER));
            muscleMassEntries.add(new Entry(x_points, weightReport.get(i).getMuscleMass().floatValue(), ChartType.MUSCLE_MASS));
            muscleQualityEntries.add(new Entry(x_points, weightReport.get(i).getMuscleQuality().floatValue(), ChartType.MUSCLE_QUALITY));
        }
        addLineSetToLineData(createLineDataSet(bodyFatEntries, "Body fat", Color.CYAN));
        addLineSetToLineData(createLineDataSet(bodyWatterEntries, "Body watter", Color.GREEN));
        addLineSetToLineData(createLineDataSet(muscleMassEntries, "Muscle mass", Color.GRAY));
        addLineSetToLineData(createLineDataSet(muscleQualityEntries, "Muscle quality", Color.DKGRAY));
    }

    @Override
    List<Entry> createEntries(List<WeightReportDTO> weightReport) {
        final List<Entry> entries = new ArrayList<>();
        for (int i = 1; i < weightReport.size(); i++) {
            float x_points = TimeUnit.MILLISECONDS.toHours(weightReport.get(i).getDate().atZone(
                    ZoneId.systemDefault()).toInstant().toEpochMilli());
            float y_points = getXValue(weightReport, i);
            entries.add(new Entry(x_points, y_points, ChartType.WEIGHT));
        }
        return entries;
    }

    @Override
    float getXValue(List<WeightReportDTO> weightReport, int index) {
        return weightReport.get(index).getWeight().floatValue();
    }

    @Override
    void setUpLineDataSet(LineDataSet lineDataSet) {
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }

    @Override
    void setUpLineYAxis(YAxis axis) {

    }
}
