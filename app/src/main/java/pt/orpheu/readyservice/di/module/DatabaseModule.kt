package pt.orpheu.readyservice.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pt.orpheu.readyservice.database.ReadyServiceDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    companion object {
        const val NAME = "ready_service.db"
    }

    @Singleton
    @Provides
    fun provideDatabase(app: Application) : ReadyServiceDatabase {
        return Room
                .databaseBuilder(app, ReadyServiceDatabase::class.java, NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideOrderDao(database: ReadyServiceDatabase) = database.getOrderDao()


    @Singleton
    @Provides
    fun provideOrderedItemsDao(database: ReadyServiceDatabase) = database.getOrderedItemsDao()

}