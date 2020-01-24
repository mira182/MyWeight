package cz.mira.myweight.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity
public class WeightLastUpdate {

    @PrimaryKey
    private Long id;

    @ColumnInfo(name = "last_updated")
    private LocalDateTime lastUpdated;
}
