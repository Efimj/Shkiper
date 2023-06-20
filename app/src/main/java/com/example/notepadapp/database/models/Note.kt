package com.example.notepadapp.database.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.*

enum class NotePosition {
    MAIN,
    ARCHIVE,
    DELETE,
}

class Note : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()

    @Index
    var header: String = ""

    @Index
    var body: String = ""
    var hashtags: RealmList<String> = realmListOf()
    var creationDate: Date = Date()
    var updateDate: Date = Date()
    var deletionDate: Date? = null

    @Index
    var isPinned: Boolean = false

    @Index
    var positionString: String = NotePosition.MAIN.name

    var position: NotePosition
        get() = NotePosition.valueOf(positionString)
        set(value) {
            positionString = value.name
        }
}