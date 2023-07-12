package com.android.notepad.database.di

import com.android.notepad.database.data.note.NoteMongoRepository
import com.android.notepad.database.data.note.NoteMongoRepositoryImpl
import com.android.notepad.database.data.reminder.ReminderMongoRepository
import com.android.notepad.database.data.reminder.ReminderMongoRepositoryImpl
import com.android.notepad.database.models.Note
import com.android.notepad.database.models.Reminder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                Note::class,
                Reminder::class,
            )
        )
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideNoteMongoRepository(realm: Realm): NoteMongoRepository {
        return NoteMongoRepositoryImpl(realm = realm)
    }

    @Singleton
    @Provides
    fun provideReminderMongoRepository(realm: Realm): ReminderMongoRepository {
        return ReminderMongoRepositoryImpl(realm = realm)
    }
}