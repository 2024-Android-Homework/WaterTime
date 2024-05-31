package com.wychlw.watertime.DataHandeler

import android.content.Context
import com.google.gson.Gson
import java.io.File
class SettingHandeler {
    private var SettingFile: File

    constructor(ctx: Context) {
        SettingFile = File(ctx.filesDir, "settingData")
    }

    fun loadSetting(): Int {
        return if (SettingFile.exists()) {
            val gson = Gson()
            val json = SettingFile.readText()
            gson.fromJson(json, Int::class.java)
        } else {
            0
        }
    }

    fun saveSetting(setting: Int) {
        val gson = Gson()
        val json = gson.toJson(setting)
        SettingFile.writeText(json)
    }
}