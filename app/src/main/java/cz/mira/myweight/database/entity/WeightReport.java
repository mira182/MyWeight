package cz.mira.myweight.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeightReport {

    @PrimaryKey
    private Long id;

    private LocalDateTime date;

    private double weight;

    private double bmi;

    private double bodyFat;

    private double visceralFat;

    private double muscleMass;

    private double muscleQuality;

    private double boneMass;

    private double bmr;

    private double metabolicAge;

    private double bodyWatter;

    private double physiqueRating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public double getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(double bodyFat) {
        this.bodyFat = bodyFat;
    }

    public double getVisceralFat() {
        return visceralFat;
    }

    public void setVisceralFat(double visceralFat) {
        this.visceralFat = visceralFat;
    }

    public double getMuscleMass() {
        return muscleMass;
    }

    public void setMuscleMass(double muscleMass) {
        this.muscleMass = muscleMass;
    }

    public double getMuscleQuality() {
        return muscleQuality;
    }

    public void setMuscleQuality(double muscleQuality) {
        this.muscleQuality = muscleQuality;
    }

    public double getBoneMass() {
        return boneMass;
    }

    public void setBoneMass(double boneMass) {
        this.boneMass = boneMass;
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
    }

    public double getMetabolicAge() {
        return metabolicAge;
    }

    public void setMetabolicAge(double metabolicAge) {
        this.metabolicAge = metabolicAge;
    }

    public double getBodyWatter() {
        return bodyWatter;
    }

    public void setBodyWatter(double bodyWatter) {
        this.bodyWatter = bodyWatter;
    }

    public double getPhysiqueRating() {
        return physiqueRating;
    }

    public void setPhysiqueRating(double physiqueRating) {
        this.physiqueRating = physiqueRating;
    }
}
