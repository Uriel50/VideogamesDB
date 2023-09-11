package com.camu.videogamesdb.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.camu.videogamesdb.data.db.model.GameEntity
import com.camu.videogamesdb.util.Constants.DATABASE_GAME_TABLE



@Dao
interface GameDao {

    //Create
    @Insert
    suspend fun insertGame(game: GameEntity)

    @Insert
    suspend fun insertGame(games: List<GameEntity>)

    //Read
    @Query("SELECT * FROM ${DATABASE_GAME_TABLE}")
    suspend fun getAllGames(): List<GameEntity>

    //Update
    @Update
    suspend fun updateGame(game: GameEntity)

    //Delete
    @Delete
    suspend fun deleteGame(game: GameEntity)
}