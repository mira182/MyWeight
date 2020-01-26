package cz.mira.myweight.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import cz.mira.myweight.database.converter.LocalDateTimeConverter;
import cz.mira.myweight.database.dao.WeightLastUpdatedDAO;
import cz.mira.myweight.database.dao.WeightReportDAO;
import cz.mira.myweight.database.entity.WeightLastUpdate;
import cz.mira.myweight.database.entity.WeightReport;

@Database(entities = {WeightLastUpdate.class, WeightReport.class}, version = 1, exportSchema = false)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WeightLastUpdatedDAO weightLastUpdateDao();

    public abstract WeightReportDAO weightReportDAO();
}
