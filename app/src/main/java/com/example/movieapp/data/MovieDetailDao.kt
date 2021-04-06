package com.example.movieapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface MovieDetailDao {
    @Query("Select * from movie where id = :id")
    fun getMovie(id:Long) : LiveData<Movie>
}