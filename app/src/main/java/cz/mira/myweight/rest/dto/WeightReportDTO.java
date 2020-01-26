package cz.mira.myweight.rest.dto;

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
public class WeightReportDTO {

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
}
