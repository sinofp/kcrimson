package top.emptystack.kcrimson

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*


//import top.emptystack.kcrimson.EventFragment.OnListFragmentInteractionListener
import top.emptystack.kcrimson.dummy.DummyContent.DummyItem

//import kotlinx.android.synthetic.main.fragment_event.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyEventRecyclerViewAdapter(
    private var mValues: List<Event>//,
//    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyEventRecyclerViewAdapter.ViewHolder>() {

//    private val mOnClickListener: View.OnClickListener
//
//    init {
//        mOnClickListener = View.OnClickListener { v ->
//            val item = v.tag as Event
//            // Notify the active callbacks interface (the activity, if the fragment is attached to
//            // one) that an item has been selected.
//            mListener?.onListFragmentInteraction(item)
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.fragment_event, parent, false)
        return ViewHolder(EventUI().createView(AnkoContext.create(parent.context, parent)))//ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.name
        holder.mContentView.text = item.ddl.toString()

//        with(holder.mView) {
//            tag = item
//            setOnClickListener(mOnClickListener)
//        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView =itemView.findViewById(EventUI.tvTitleId)// mView.item_number
        val mContentView: TextView = itemView.findViewById(EventUI.tvYearId)// mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

    fun removeAt(index: Int): String {
        val deletedName = mValues[index].name
        mValues -= mValues[index]
        notifyItemRemoved(index)
        return deletedName
    }
}

class EventUI : AnkoComponent<ViewGroup> {

    companion object {
        val tvTitleId = 1
        val tvYearId = 2
    }

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, wrapContent)
            padding = dip(16)

            textView {
                id = tvTitleId
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                textSize = 16f // <- it is sp, no worries
                textColor = Color.BLACK
            }

            textView {
                id = tvYearId
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                textSize = 14f
            }
        }
    }
}