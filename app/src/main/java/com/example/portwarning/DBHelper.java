package com.example.portwarning;

import com.orm.SugarRecord;

import java.util.List;

public class DBHelper<T extends SugarRecord> implements IDBService<T> {

    @Override
    public T getValuesByLatLong(double lat, double lng, Class<T> type) {
        List<T> items = T.find(type, "lat = ? and lon = ?",
                new String[]{
                        String.valueOf(lat), String.valueOf(lng)
                }, null, null, null);
        if (items != null && items.size() > 0) {
            return items.get(0);
        }
        return null;
    }

}
