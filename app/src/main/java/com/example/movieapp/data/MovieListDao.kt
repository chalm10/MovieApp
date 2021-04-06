package com.example.movieapp.data


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieListDao {
    @Query("Select * from movie order by release_date desc")
    fun getMovies() : LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(movies : List<Movie>)

    @Query("Delete from movie")
    suspend fun deleteAllData()
}