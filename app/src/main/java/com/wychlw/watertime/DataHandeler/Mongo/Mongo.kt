package com.wychlw.watertime.DataHandeler.Mongo
//
//import com.mongodb.ConnectionString
//import com.mongodb.MongoClientSettings
//import com.mongodb.ServerApi
//import com.mongodb.ServerApiVersion
//import com.wychlw.watertime.Record
//import com.mongodb.kotlin.client.MongoClient
//import com.wychlw.watertime.reminder.periodicReminder
//import com.wychlw.watertime.reminder.timingReminder
//import org.bson.Document
//
//class MongoHandler {
//
//    companion object {
//
//        var client: MongoHandler? = null
//
//        private val connectionString = "mongodb+srv://lingwang:97Y5RRhnVtSTwqgf@cluster0.me2ruzu.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
//        fun getInstance(): MongoHandler {
//            if (client == null) {
//                client = MongoHandler(connectionString)
//            }
//            return client!!
//        }
//
//        fun getInstance(selfConnectionString: String): MongoHandler {
//            if (client == null) {
//                client = MongoHandler(selfConnectionString)
//            }
//            return client!!
//        }
//    }
//
//    private var mongoClient: MongoClient? = null
//
//    private constructor(connectionString: String) {
//        connect(connectionString)
//    }
//
//    private fun finalize() {
//        close()
//    }
//
//    private fun connect(connectionString: String) {
//        if (mongoClient != null) {
//            return
//        }
//
//        val serverApi = ServerApi.builder()
//            .version(ServerApiVersion.V1)
//            .build()
//
//        val connStr = ConnectionString(connectionString)
//        val mongoClientSettings = MongoClientSettings.builder()
//            .applyConnectionString(connStr)
//            .serverApi(serverApi)
//            .build()
//
//        MongoClient.create(mongoClientSettings).use { mongoClient ->
//            val database = mongoClient.getDatabase("watertime")
//            database.runCommand(Document("ping", 1))
//            println("Pinged your deployment. You successfully connected to MongoDB!")
//        }
//    }
//
//    private fun close() {
//        mongoClient?.close()
//    }
//
//    fun saveRecord(
//        record: List<Record>
//    ) {
//        if (mongoClient == null) {
//            throw Exception("MongoClient is not connected")
//        }
//        val db = mongoClient!!.getDatabase("watertime")
//        val collection = db.getCollection<Document>("record")
//
//        try {
//            // if the collection does not exist, create it
//            if (collection.countDocuments() == 0L) {
//                collection.insertOne(Document("record", record))
//            } else {
//                // otherwise, update the existing record
//                collection.updateOne(Document(), Document("\$set", Document("record", record)))
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun loadRecord(): List<Record> {
//        if (mongoClient == null) {
//            throw Exception("MongoClient is not connected")
//        }
//        val db = mongoClient!!.getDatabase("watertime")
//        val collection = db.getCollection<Document>("record")
//
//        try {
//            val result = collection.find().firstOrNull()
//            return result?.get("record") as List<Record>? ?: mutableListOf()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return mutableListOf()
//        }
//    }
//
//    fun saveIntervalReminder(list: List<periodicReminder>) {
//        if (mongoClient == null) {
//            throw Exception("MongoClient is not connected")
//        }
//        val db = mongoClient!!.getDatabase("watertime")
//        val collection = db.getCollection<Document>("intervalReminder")
//
//        try {
//            // if the collection does not exist, create it
//            if (collection.countDocuments() == 0L) {
//                collection.insertOne(Document("intervalReminder", list))
//            } else {
//                // otherwise, update the existing record
//                collection.updateOne(Document(), Document("\$set", Document("intervalReminder", list)))
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun loadIntervalReminder(): List<periodicReminder> {
//        if (mongoClient == null) {
//            throw Exception("MongoClient is not connected")
//        }
//        val db = mongoClient!!.getDatabase("watertime")
//        val collection = db.getCollection<Document>("intervalReminder")
//
//        try {
//            val result = collection.find().firstOrNull()
//            return result?.get("intervalReminder") as List<periodicReminder>? ?: mutableListOf()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return mutableListOf()
//        }
//    }
//
//    fun saveTimingReminder(list: List<timingReminder>) {
//        if (mongoClient == null) {
//            throw Exception("MongoClient is not connected")
//        }
//        val db = mongoClient!!.getDatabase("watertime")
//        val collection = db.getCollection<Document>("timingReminder")
//
//        try {
//            // if the collection does not exist, create it
//            if (collection.countDocuments() == 0L) {
//                collection.insertOne(Document("timingReminder", list))
//            } else {
//                // otherwise, update the existing record
//                collection.updateOne(Document(), Document("\$set", Document("timingReminder", list)))
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun loadTimingReminder(): List<timingReminder> {
//        if (mongoClient == null) {
//            throw Exception("MongoClient is not connected")
//        }
//        val db = mongoClient!!.getDatabase("watertime")
//        val collection = db.getCollection<Document>("timingReminder")
//
//        try {
//            val result = collection.find().firstOrNull()
//            return result?.get("timingReminder") as List<timingReminder>? ?: mutableListOf()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return mutableListOf()
//        }
//    }
//
//}
//
