package cz.mira.myweight.charts;

import android.content.Context;
import android.view.View;

import java.util.List;

import cz.mira.myweight.rest.dto.WeightReportDTO;

public class MuscleMassChart extends AbstractChart {

    public MuscleMassChart(Context context, View view, List<WeightReportDTO> weightReport) {
        super(context, view, weightReport);
    }

    @Override
    float getYAxisDataList(List<WeightReportDTO> weightReport, int index) {
        return weightReport.get(index).getMuscleMass().floatValue();
    }
}
