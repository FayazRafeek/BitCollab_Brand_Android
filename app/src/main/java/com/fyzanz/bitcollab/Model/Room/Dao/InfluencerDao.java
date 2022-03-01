package com.fyzanz.bitcollab.Model.Room.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.fyzanz.bitcollab.Model.Data.FavInfluencer;
import com.fyzanz.bitcollab.Model.Data.Influencer;

import java.util.List;

@Dao
public interface InfluencerDao {

    @Query("SELECT * FROM favInfluencers")
    List<FavInfluencer> getAll();

    @Query("SELECT * FROM favInfluencers WHERE infId =:infId")
    FavInfluencer getFavInfById(String infId);

    @Insert
    void insert(FavInfluencer influencers);

    @Delete
    void delete(FavInfluencer user);
}
