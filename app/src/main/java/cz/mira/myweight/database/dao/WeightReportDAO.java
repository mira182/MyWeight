package cz.mira.myweight.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cz.mira.myweight.database.entity.WeightReport;

@Dao
public interface WeightReportDAO {


    @Query("SELECT * FROM weightreport")
    List<WeightReport> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeightReport weightReport);

    @Delete
    void delete(WeightReport weightReport);

    @Query("DELETE FROM weightreport")
    void deleteAll();
}