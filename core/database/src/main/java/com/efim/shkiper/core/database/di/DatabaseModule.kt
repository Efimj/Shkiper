package com.efim.shkiper.core.database.di

import android.content.Context
import com.efim.shkiper.core.database.data.note.NoteMongoRepository
import com.efim.shkiper.core.database.data.note.NoteMongoRepositoryImpl
import com.efim.shkiper.core.database.data.reminder.ReminderMongoRepository
import com.efim.shkiper.core.database.data.reminder.ReminderMongoRepositoryImpl
import com.efim.shkiper.core.domain.model.Note
import com.efim.shkiper.core.domain.model.Reminder
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