package cz.mira.myweight.charts;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import cz.mira.myweight.rest.dto.WeightReportDTO;

public class BmiChart extends AbstractChart {

    public BmiChart(Context context, View view, List<WeightReportDTO> weightReport) {
        super(context, view, weightReport);
        chartName = "BMI";
    }

    @Override
    float getYAxisDataList(List<WeightReportDTO> weightReport, int index) {
        return weightReport.get(index).getBmi().floatValue();
    }

    @Override
    void setDataSetMode(LineDataSet lineDataSet) {
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }

    @Override
    String setPrintStringForXValue(float selectedYValue) {
        return chartName + ": " + selectedYValue;
    }


}
