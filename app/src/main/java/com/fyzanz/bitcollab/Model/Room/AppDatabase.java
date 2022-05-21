package com.fyzanz.bitcollab.Model.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Room.Dao.BrandDao;
import com.fyzanz.bitcollab.Model.Room.Dao.InfluencerDao;
import com.fyzanz.bitcollab.R;

@Database(entities = {Influencer.class, Brand.class}, version = 7)
@TypeConverters(Converted.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE = null;

    public static void createDatabase(Context context){
        INSTANCE = Room.databaseBuilder(context,
                AppDatabase.class, context.getString(R.string.ROOM_DB_NAME))
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
    public static AppDatabase getINSTANCE() {
        return INSTANCE;
    }

    public abstract InfluencerDao influencerDao();
    public abstract BrandDao brandDao();
}
