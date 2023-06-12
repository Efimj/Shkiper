package com.example.notepadapp.database.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.LocalTime

enum class RepeatMode {
    NONE,
    EVERYDAY,
    EVERYWEEK,
    EVERYMONTH,
    EVERYYEAR,
}

class Reminder  : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    @Index
    var noteId: ObjectId = ObjectId.invoke()
    var time: LocalTime = LocalTime.now()
    var date:LocalDate = LocalDate.now()
    var repeat: RepeatMode = RepeatMode.NONE
}