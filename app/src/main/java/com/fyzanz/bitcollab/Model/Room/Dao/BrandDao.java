package com.fyzanz.bitcollab.Model.Room.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.fyzanz.bitcollab.Model.Data.Brand;

import java.util.List;

@Dao
public interface BrandDao {

    @Query("SELECT * FROM favBrands")
    List<Brand> getAllFav();

    @Query("SELECT * FROM favBrands WHERE brandId =:brandId")
    Brand getFavBrandById(String brandId);

    @Insert
    void insert(Brand brand);

    @Delete
    void delete(Brand brand);
}
