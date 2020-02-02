package cz.mira.myweight.charts;

import android.content.Context;
import android.view.View;

import java.util.List;

import cz.mira.myweight.rest.dto.WeightReportDTO;

public enum ChartType {
    WEIGHT("Weight", "kg") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new WeightLineChart(context, view, weightReport);
        }
    },
    BMI("BMI") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new BmiLineChart(context, view, weightReport);
        }
    },
    BODY_FAT("Body fat", "%") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new BodyFatLineChart(context, view, weightReport);
        }
    },
    VISCERAL_FAT("Visceral fat") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new VisceralFatLineChart(context, view, weightReport);
        }
    },
    MUSCLE_MASS("Muscle mass", "kg") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new MuscleMassLineChart(context, view, weightReport);
        }
    },
    MUSCLE_QUALITY("Muscle quality") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new MuscleQualityLineChart(context, view, weightReport);
        }
    },
    BONE_MASS("Bone mass", "kg") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new BoneMassLineChart(context, view, weightReport);
        }
    },
    BMR("BMR", "kcal") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new BmrLineChart(context, view, weightReport);
        }
    },
    METABOLIC_AGE("Metabolic age", "years") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new MetabolicAgeLineChart(context, view, weightReport);
        }
    },
    BODY_WATTER("Body watter", "%") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new BodyWatterLineChart(context, view, weightReport);
        }
    },
    PHYSIQUE_RATING("Physique rating") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new PhysiqueRatingLineChart(context, view, weightReport);
        }
    },
    MULTI_LINE("Multi line") {
        @Override
        public AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new MultiLineChart(context, view, weightReport);
        }
    };

    private String chartName;

    private String unit;

    ChartType(String chartName, String unit) {
        this.chartName = chartName;
        this.unit = unit;
    }

    ChartType(String chartName) {
        this.chartName = chartName;
        this.unit = "";
    }

    public String getChartName() {
        return chartName;
    }

    public String getUnit() {
        return unit;
    }

    public abstract AbstractLineChart createChart(Context context, View view, List<WeightReportDTO> weightReport);
}
