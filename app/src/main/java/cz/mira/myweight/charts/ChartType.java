package cz.mira.myweight.charts;

import android.content.Context;
import android.view.View;

import java.util.List;

import cz.mira.myweight.rest.dto.WeightReportDTO;

public enum ChartType {
    WEIGHT {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new WeightChart(context, view, weightReport);
        }
    },
    BMI {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new BmiChart(context, view, weightReport);
        }
    },
    BODY_FAT {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new BodyFatChart(context, view, weightReport);
        }
    },
    VISCERAL_FAT {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new VisceralFatChart(context, view, weightReport);
        }
    },
    MUSCLE_MASS {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new MuscleMassChart(context, view, weightReport);
        }
    },
    MUSCLE_QUALITY {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new MuscleQualityChart(context, view, weightReport);
        }
    },
    BONE_MASS {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new BoneMassChart(context, view, weightReport);
        }
    },
    BMR {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new BmrChart(context, view, weightReport);
        }
    },
    METABOLIC_AGE {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new MetabolicAgeChart(context, view, weightReport);
        }
    },
    BODY_WATTER {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new BodyWatterChart(context, view, weightReport);
        }
    },
    PHYSIQUE_RATING {
        @Override
        public AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport) {
            return new PhysiqueRatingChart(context, view, weightReport);
        }
    };

    public abstract AbstractChart createChart(Context context, View view, List<WeightReportDTO> weightReport);
}
