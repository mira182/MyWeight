package cz.mira.myweight.rest.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeightReportDTO implements Parcelable {

    private LocalDateTime date;

    private Double weight;

    private Double bmi;

    private Double bodyFat;

    private Double visceralFat;

    private Double muscleMass;

    private Double muscleQuality;

    private Double boneMass;

    private Double bmr;

    private Double metabolicAge;

    private Double bodyWatter;

    private Double physiqueRating;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.date);
        dest.writeValue(this.weight);
        dest.writeValue(this.bmi);
        dest.writeValue(this.bodyFat);
        dest.writeValue(this.visceralFat);
        dest.writeValue(this.muscleMass);
        dest.writeValue(this.muscleQuality);
        dest.writeValue(this.boneMass);
        dest.writeValue(this.bmr);
        dest.writeValue(this.metabolicAge);
        dest.writeValue(this.bodyWatter);
        dest.writeValue(this.physiqueRating);
    }

    protected WeightReportDTO(Parcel in) {
        this.date = (LocalDateTime) in.readSerializable();
        this.weight = (Double) in.readValue(Double.class.getClassLoader());
        this.bmi = (Double) in.readValue(Double.class.getClassLoader());
        this.bodyFat = (Double) in.readValue(Double.class.getClassLoader());
        this.visceralFat = (Double) in.readValue(Double.class.getClassLoader());
        this.muscleMass = (Double) in.readValue(Double.class.getClassLoader());
        this.muscleQuality = (Double) in.readValue(Double.class.getClassLoader());
        this.boneMass = (Double) in.readValue(Double.class.getClassLoader());
        this.bmr = (Double) in.readValue(Double.class.getClassLoader());
        this.metabolicAge = (Double) in.readValue(Double.class.getClassLoader());
        this.bodyWatter = (Double) in.readValue(Double.class.getClassLoader());
        this.physiqueRating = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<WeightReportDTO> CREATOR = new Parcelable.Creator<WeightReportDTO>() {
        @Override
        public WeightReportDTO createFromParcel(Parcel source) {
            return new WeightReportDTO(source);
        }

        @Override
        public WeightReportDTO[] newArray(int size) {
            return new WeightReportDTO[size];
        }
    };
}
