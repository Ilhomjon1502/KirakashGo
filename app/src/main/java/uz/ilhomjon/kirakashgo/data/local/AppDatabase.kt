package uz.ilhomjon.kirakashgo.data.local

import androidx.room.RoomDatabase

abstract class AppDatabase:RoomDatabase() {

    abstract fun dbDao():DbDao
}