package uz.ilhomjon.kirakashgo.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room
            .databaseBuilder(context, AppDatabase::class.java, "my_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideDbDao(appDatabase: AppDatabase):DbDao = appDatabase.dbDao()
}