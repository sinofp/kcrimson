package top.emptystack.kcrimson

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import java.util.*
import kotlin.collections.ArrayList

data class Event(val name:String, val ddl: Long)

class EventFragment : Fragment() {

    private var columnCount = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                val now = Calendar.getInstance()
                now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
                val currentTime = now.timeInMillis
                Log.d("time", "currentTime is $currentTime")

                val events = ArrayList<Event>()
                context.database.use {
                    val parser = rowParser { _: Int, name: String, ddl: Long -> Pair(name, ddl) }
                    val rawList = select("Todo").parseList(parser)
                    for (raw in rawList) {
                        Log.d("time", "savedTime is ${raw.second}")
                        events.add(Event(raw.first, (raw.second - currentTime)/86400000))
                    }
                }
                adapter = MyEventRecyclerViewAdapter(events)//, listener)

                //左滑删除事件
                val swipeHandler = object : SwipeToDeleteCallback(context) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = adapter as MyEventRecyclerViewAdapter
                        val deletedName = adapter.removeAt(viewHolder.adapterPosition)
                        context.database.use {
                            delete("Todo", "name = {deletedName}", "deletedName" to deletedName)
                        }
                    }
                }
                val itemTouchHelper = ItemTouchHelper(swipeHandler)
                itemTouchHelper.attachToRecyclerView(view)
            }
        }
        return view
    }
}
