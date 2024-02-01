package com.jobik.shkiper.database.di

import android.content.Context
import androidx.annotation.Keep
import com.jobik.shkiper.database.data.note.NoteMongoRepository
import com.jobik.shkiper.database.data.note.NoteMongoRepositoryImpl
import com.jobik.shkiper.database.data.reminder.ReminderMongoRepository
import com.jobik.shkiper.database.data.reminder.ReminderMongoRepositoryImpl
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.Reminder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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
//            .schemaVersion(0)
            .build()
        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideNoteMongoRepository(realm: Realm, @ApplicationContext appContext: Context): NoteMongoRepository {
        return NoteMongoRepositoryImpl(realm = realm, context = appContext)
    }

    @Singleton
    @Provides
    fun provideReminderMongoRepository(realm: Realm, @ApplicationContext appContext: Context): ReminderMongoRepository {
        return ReminderMongoRepositoryImpl(realm = realm, context = appContext)
    }
}