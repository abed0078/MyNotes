package com.example.mynotes.dao

import androidx.room.*
import com.example.mynotes.entities.Notes

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC ")
    suspend fun getAllNotes(): List<Notes>

    @Query("SELECT * FROM notes WHERE id=:id ")
    suspend fun getSpecificNotes(id:Int): Notes

    @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotes(note: Notes)

    @Delete
    suspend fun deleteNotes(note: Notes)

    @Query("DELETE FROM notes WHERE id=:id ")
    suspend fun deleteSpecificNotes(id:Int)

    @Update
    suspend fun updateNote(note: Notes)
}