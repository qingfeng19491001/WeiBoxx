package com.example.weiboxx.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weiboxx.database.dao.PostDao
import com.example.weiboxx.data.model.Post

@Database(
    entities = [Post::class],
    version = 2, // 重要：增加版本号从1到2
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 定义从版本1到版本2的迁移策略
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 安全添加新列（不删除旧数据）
                database.execSQL("ALTER TABLE posts ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")

                // 迁移后更新现有数据的createdAt值
                database.execSQL("UPDATE posts SET createdAt = CAST(strftime('%s', 'now') * 1000")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "social_media_database"
                )
                    .addMigrations(MIGRATION_1_2) // 添加迁移策略
                    .fallbackToDestructiveMigration() // 开发阶段可选：如果迁移失败则重新创建数据库
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
