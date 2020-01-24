package cz.mira.myweight.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cz.mira.myweight.database.entity.WeightLastUpdate;

@Dao
public interface WeightLastUpdatedDAO {

    @Query("SELECT * FROM weightlastupdate ORDER BY id DESC LIMIT 1")
    WeightLastUpdate getLastWeightUpdate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeightLastUpdate weightLastUpdate);

    @Delete
    void delete(WeightLastUpdate weightLastUpdate);
}