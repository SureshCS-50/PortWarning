package com.example.portwarning;

import com.orm.SugarRecord;

import java.util.List;

public interface IDBService<T extends SugarRecord> {

    T getValuesByLatLong(double lat, double lng, Class<T> type);

}