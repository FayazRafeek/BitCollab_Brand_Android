package com.fyzanz.bitcollab.Model.Room;

import androidx.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converted {

    @TypeConverter
    public String listToJsonString(List<String> value){
        return new Gson().toJson(value);
    }

    @TypeConverter
    public ArrayList<String> jsonStringToList(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
}
