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
            val s = SettingFile.readText()
            return s.toInt()
        } else {
            0
        }
    }

    fun saveSetting(setting: Int) {
           SettingFile.writeText(setting.toString())
    }
}