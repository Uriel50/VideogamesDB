package com.camu.videogamesdb.application

import android.app.Application
import com.camu.videogamesdb.data.GameRepository
import com.camu.videogamesdb.data.db.GameDatabase


class VideogamesDBApp(): Application() {
    private val database by lazy{
        GameDatabase.getDatabase(this@VideogamesDBApp)
    }

    val repository by lazy{
        GameRepository(database.gameDao())
    }
}