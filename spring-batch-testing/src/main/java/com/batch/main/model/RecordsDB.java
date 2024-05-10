package com.batch.main.model;

import java.util.List;

public class RecordsDB {

    private List<RecordDB> recordDBList;

    public RecordsDB() {
    }

    public List<RecordDB> getRecordDBList() {
        return recordDBList;
    }

    public void setRecordDBList(List<RecordDB> recordDBList) {
        this.recordDBList = recordDBList;
    }
}
