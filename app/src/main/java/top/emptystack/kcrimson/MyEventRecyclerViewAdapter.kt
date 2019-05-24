package top.emptystack.kcrimson

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*

class MyEventRecyclerViewAdapter(
    private var mValues: List<Event>
) : RecyclerView.Adapter<MyEventRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EventUI().createView(AnkoContext.create(parent.context, parent)))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.name.text = item.name
        holder.ddl.text = "距离DDL只剩${item.ddl}天了！"
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val name: TextView =itemView.findViewById(EventUI.tvName)// mView.item_number
        val ddl: TextView = itemView.findViewById(EventUI.tvDDL)// mView.content

        override fun toString(): String {
            return super.toString() + " '" + ddl.text + "'"
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
        val tvName = 1
        val tvDDL = 2
    }

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, wrapContent)
            padding = dip(16)

            textView {
                id = tvName
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                textSize = 16f
                textColor = Color.BLACK
            }

            textView {
                id = tvDDL
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                textSize = 14f
            }
        }
    }
}