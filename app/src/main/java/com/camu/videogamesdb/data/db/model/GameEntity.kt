package com.camu.videogamesdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.camu.videogamesdb.util.Constants



@Entity(tableName = Constants.DATABASE_GAME_TABLE)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    val id: Long = 0,

    @ColumnInfo(name = "game_title")
    var title: String,

    @ColumnInfo(name = "game_genre")
    var genre: String,

    @ColumnInfo(name = "game_assessment")
    var assessment: String,

    @ColumnInfo(name = "game_year")
    var year: String
)
