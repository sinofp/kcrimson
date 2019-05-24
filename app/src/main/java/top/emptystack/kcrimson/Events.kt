package top.emptystack.kcrimson

import android.content.Context
import android.util.Log
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import java.util.*
import kotlin.collections.ArrayList

data class Event(val name:String, val ddl: Long)

fun getStoredEvents(context: Context?): List<Event> {
    val events = ArrayList<Event>()

    val now = Calendar.getInstance()
    now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
    val currentTime = now.timeInMillis

    context!!.database.use {
        val parser = rowParser { _: Int, name: String, ddl: Long -> Pair(name, ddl) }
        val rawList = select("Todo").parseList(parser)
        for (raw in rawList) {
            val savedTime = raw.second
            Log.d("time", "savedTime is $savedTime")
            if (savedTime < currentTime) {
                delete("Todo", "name = {deletedName}", "deletedName" to raw.first)
            } else {
                events.add(Event(raw.first, (savedTime - currentTime)/86400000))
            }
        }
    }

    return events.sortedByDescending { it.ddl }
}
