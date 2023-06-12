package com.example.notepadapp.database.di

import com.example.notepadapp.database.data.note.NoteMongoRepository
import com.example.notepadapp.database.data.note.NoteMongoRepositoryImpl
import com.example.notepadapp.database.data.reminder.ReminderMongoRepository
import com.example.notepadapp.database.data.reminder.ReminderMongoRepositoryImpl
import com.example.notepadapp.database.models.Note
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

    @Override
    @Singleton
    @Provides
    fun provideReminderMongoRepository(realm: Realm): ReminderMongoRepository {
        return ReminderMongoRepositoryImpl(realm = realm)
    }
}