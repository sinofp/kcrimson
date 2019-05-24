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
import android.widget.Adapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.support.v4.toast
import java.util.*

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

    override fun onResume() {
        super.onResume()
        refDate()
    }

    fun refDate() {
        with(view as RecyclerView) {
            val events = getStoredEvents(context)
            adapter = MyEventRecyclerViewAdapter(events)
        }
    }
}
