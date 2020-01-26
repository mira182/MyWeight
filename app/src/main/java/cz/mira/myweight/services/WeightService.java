package cz.mira.myweight.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import cz.mira.myweight.database.AppDatabase;
import cz.mira.myweight.database.entity.WeightLastUpdate;
import cz.mira.myweight.database.entity.WeightReport;

public class WeightService {

    private GmailService gmailService;

    private AppDatabase db;

    public WeightService(GmailService gmailService, AppDatabase db) {
        this.gmailService = gmailService;
        this.db = db;
    }

    public boolean saveAttachment() throws IOException, GeneralSecurityException {
        final byte[] csvFileBytes = gmailService.getAttachmentFromMailByQuery();

        if (csvFileBytes == null) throw new IOException("Failed to get attachment from email.");

        db.weightReportDAO().deleteAll();

        CSVParser csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(new InputStreamReader(new ByteArrayInputStream(csvFileBytes)));

        for (CSVRecord record : csvParser) {
            String dateString = record.get("Date");
            String weight = record.get("Weight (kg)");
            String bmi = record.get("BMI");
            String bodyFat = record.get("Body Fat (%)");
            String viscFat = record.get("Visc Fat");
            String muscleMass = record.get("Muscle Mass (kg)");
            String muscleQuality = record.get("Muscle Quality");
            String boneMass = record.get("Bone Mass (kg)");
            String bmr = record.get("BMR (kcal)");
            String metabAge = record.get("Metab Age");
            String bodyWatter = record.get("Body Water (%)");
            String phyRating = record.get("Physique Rating");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(dateString, formatter);

            db.weightReportDAO().insert(WeightReport.builder()
                    .boneMass(Double.valueOf(boneMass))
                    .muscleMass(Double.valueOf(muscleMass))
                    .bmi(Double.valueOf(bmi))
                    .bmr(Double.valueOf(bmr))
                    .bodyFat(Double.valueOf(bodyFat))
                    .bodyWatter(Double.valueOf(bodyWatter))
                    .metabolicAge(Double.valueOf(metabAge))
                    .muscleQuality(Double.valueOf(muscleQuality))
                    .physiqueRating(Double.valueOf(phyRating))
                    .visceralFat(Double.valueOf(viscFat))
                    .weight(Double.valueOf(weight))
                    .date(date)
                    .build());
        }

        db.weightLastUpdateDao().insert(WeightLastUpdate.builder()
                .lastUpdated(LocalDateTime.now())
                .build());

        return true;
    }

    public List<WeightReport> getWeightReport() {
        return db.weightReportDAO().getAll();
    }
}
