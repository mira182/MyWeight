package cz.mira.myweight.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import cz.mira.myweight.database.entity.WeightLastUpdate;

@Dao
public interface WeightLastUpdatedDAO {

    @Query("SELECT * FROM weightlastupdate ORDER BY id DESC LIMIT 1")
    LiveData<WeightLastUpdate> getLastWeightUpdate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeightLastUpdate weightLastUpdate);
}
