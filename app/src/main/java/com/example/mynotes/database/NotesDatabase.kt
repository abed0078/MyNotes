    package com.example.mynotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mynotes.dao.NoteDao
import com.example.mynotes.entities.Notes

@Database(entities = [Notes::class],version = 1,exportSchema = false)
abstract class NotesDatabase:RoomDatabase() {

    companion object {
        /* @Volatile
        var notesDatabase:NotesDatabase?=null
        @Synchronized
        fun getDatabase(context:Context):NotesDatabase{
            if(notesDatabase!=null) {
                notesDatabase = Room.databaseBuilder(
                        context, NotesDatabase::class.java, "notes.db"
                ).build()
            }
            return notesDatabase!!*/

        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
    abstract fun noteDao():NoteDao

}