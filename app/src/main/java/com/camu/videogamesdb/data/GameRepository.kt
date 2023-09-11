package com.camu.videogamesdb.data

import com.camu.videogamesdb.data.db.GameDao
import com.camu.videogamesdb.data.db.model.GameEntity
import java.time.Year


class GameRepository(private val gameDao: GameDao) {

    suspend fun insertGame(game: GameEntity){
        gameDao.insertGame(game)
    }

    suspend fun insertGame(title: String, genre: String, assessment: String, year: String){
        gameDao.insertGame(GameEntity(title = title, genre = genre, assessment = assessment, year = year))
    }

    suspend fun getAllGames(): List<GameEntity> = gameDao.getAllGames()

    suspend fun updateGame(game: GameEntity){
        gameDao.updateGame(game)
    }

    suspend fun deleteGame(game: GameEntity){
        gameDao.deleteGame(game)
    }


}