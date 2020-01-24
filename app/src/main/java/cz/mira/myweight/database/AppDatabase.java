package cz.mira.myweight.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import cz.mira.myweight.database.dao.WeightLastUpdatedDAO;
import cz.mira.myweight.database.entity.WeightLastUpdate;

@Database(entities = {WeightLastUpdate.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WeightLastUpdatedDAO weightLastUpdateDao();
}
