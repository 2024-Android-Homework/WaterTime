package com.wychlw.watertime.DataHandeler

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
//import com.wychlw.watertime.DataHandeler.Mongo.MongoHandler
import com.wychlw.watertime.Record
import com.wychlw.watertime.reminder.periodicReminder
import com.wychlw.watertime.reminder.timingReminder
import java.io.File

class SyncHandeler {

//    private val dbHandeler: MongoHandler;

    constructor(connString: String?) {
        TODO()
//        if (connString == null) {
//            dbHandeler = MongoHandler.getInstance()
//        } else {
//            dbHandeler = MongoHandler.getInstance(connString)
//        }
    }

    fun syncToRemote(ctx: Context) {
        TODO()
//        val recordHandeler = RecordHandeler(ctx)
//        val recordList = recordHandeler.loadRecord()
//        dbHandeler.saveRecord(recordList)
//
//        val intervalFile = File(ctx.filesDir, "intervalState")
//        val intervalList: List<periodicReminder> = if (intervalFile.exists()) {
//            val gson = Gson()
//            val json = intervalFile.readText()
//            val typeToken = object : TypeToken<List<periodicReminder>>() {}
//            gson.fromJson(json, typeToken.type)
//        } else {
//            mutableListOf()
//        }
//        dbHandeler.saveIntervalReminder(intervalList)
//
//        val timingFile = File(ctx.filesDir, "timingState")
//        val timingList: List<timingReminder> = if (timingFile.exists()) {
//            val gson = Gson()
//            val json = timingFile.readText()
//            val typeToken = object : TypeToken<List<timingReminder>>() {}
//            gson.fromJson(json, typeToken.type)
//        } else {
//            mutableListOf()
//        }
//        dbHandeler.saveTimingReminder(timingList)
    }

    fun syncFromRemote(ctx: Context): Triple<List<Record>, List<periodicReminder>, List<timingReminder>>{
        TODO()
//        val recordHandeler = RecordHandeler(ctx)
//        val recordList = dbHandeler.loadRecord()
//        recordHandeler.saveRecord(recordList)
//
//        val intervalList = dbHandeler.loadIntervalReminder()
//        val intervalFile = File(ctx.filesDir, "intervalState")
//        val gson = Gson()
//        val json = gson.toJson(intervalList)
//        intervalFile.writeText(json)
//
//        val timingList = dbHandeler.loadTimingReminder()
//        val timingFile = File(ctx.filesDir, "timingState")
//        val json2 = gson.toJson(timingList)
//        timingFile.writeText(json2)
//
//        return Triple(recordList, intervalList, timingList)
    }

}