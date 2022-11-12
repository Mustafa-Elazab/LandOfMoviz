package com.example.landofmoviz.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.landofmoviz.data.local.dao.MovieDao
import com.example.landofmoviz.data.local.dao.TvDao
import com.example.landofmoviz.data.local.entity.FavoriteMovieEntity
import com.example.landofmoviz.data.local.entity.FavoriteTvEntity


@Database(entities = [FavoriteMovieEntity::class, FavoriteTvEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvDao(): TvDao
}