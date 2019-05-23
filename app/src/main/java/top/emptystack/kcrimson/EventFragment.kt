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
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Event(val name:String, val ddl: Long)

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [EventFragment.OnListFragmentInteractionListener] interface.
 */
class EventFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

//    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

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
                //todo
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

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnListFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
//        }
//    }

//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
//    interface OnListFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onListFragmentInteraction(item: DummyItem?)
//    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            EventFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
