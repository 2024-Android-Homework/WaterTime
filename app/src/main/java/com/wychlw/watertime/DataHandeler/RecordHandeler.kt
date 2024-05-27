package com.wychlw.watertime.DataHandeler

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wychlw.watertime.Record
import java.io.File
class RecordHandeler {

    private var RecordFile: File;

    constructor(ctx: Context) {
        RecordFile = File(ctx.filesDir, "recordData")
    }

    fun loadRecord(): List<Record> {
        return if (RecordFile.exists()) {
            val gson = Gson()
            val json = RecordFile.readText()
            val typeToken = object : TypeToken<List<Record>>() {}
            gson.fromJson(json, typeToken.type)
        } else {
            mutableListOf()
        }
    }

    fun saveRecord(record: List<Record>) {
        val gson = Gson()
        val json = gson.toJson(record)
        RecordFile.writeText(json)
    }
}