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
        dest.writeValue(date);
        dest.writeDouble(weight);
        dest.writeDouble(bmi);
        dest.writeDouble(bodyFat);
        dest.writeDouble(visceralFat);
        dest.writeDouble(muscleMass);
        dest.writeDouble(muscleQuality);
        dest.writeDouble(boneMass);
        dest.writeDouble(bmr);
        dest.writeDouble(metabolicAge);
        dest.writeDouble(bodyWatter);
        dest.writeDouble(physiqueRating);
    }
}
