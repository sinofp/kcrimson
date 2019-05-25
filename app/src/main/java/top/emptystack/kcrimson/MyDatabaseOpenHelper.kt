package top.emptystack.kcrimson

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import java.util.*

class MyDatabaseOpenHelper private constructor(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {
    init {
        instance = this
    }

    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: MyDatabaseOpenHelper(ctx.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable("Todo", true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE,
            "name" to TEXT,
            "ddl" to INTEGER)
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR, 5)
        c.set(Calendar.HOUR, 1)
        c.set(Calendar.MINUTE, 0)
        db.insert("Todo", "name" to "点击ViewList查看待办列表", "ddl" to c.timeInMillis)
        c.add(Calendar.DAY_OF_YEAR, 10)
        db.insert("Todo", "name" to "在ViewList中左滑事件可以删除", "ddl" to c.timeInMillis)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("Todo", true)
    }
}

val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(this)


