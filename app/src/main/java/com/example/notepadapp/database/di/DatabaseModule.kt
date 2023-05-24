package com.example.notepadapp.database.di

import com.example.notepadapp.database.data.NoteMongoRepository
import com.example.notepadapp.database.data.NoteMongoRepositoryImpl
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
    fun provideMongoRepository(realm: Realm): NoteMongoRepository {
        return NoteMongoRepositoryImpl(realm = realm)
    }

}