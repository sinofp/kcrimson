package top.emptystack.kcrimson

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entries: ArrayList<PieEntry> = arrayListOf()

        val now = Calendar.getInstance()
        now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
        val currentTime = now.timeInMillis
        Log.d("home", "currentTime is $currentTime")

        context!!.database.use {
            val parser = rowParser { _: Int, name: String, ddl: Long -> Pair(name, ddl) }
            val rawList = select("Todo").parseList(parser)
            for (raw in rawList) {
                Log.d("time", "savedTime is ${raw.second}")
                val ddlInDays = (raw.second - currentTime)/86400000
                Log.d("home", ddlInDays.toString())
                entries.add(PieEntry(1/ddlInDays.toFloat(), raw.first))
            }
        }

        entries.sortBy { it.value }
        for (entry in entries) {
            Log.d("chart", "value:${entry.value}, label${entry.label}")
        }
        val closest = entries.last().label

        val set = PieDataSet(entries, "事件名")
        set.colors = ColorTemplate.COLORFUL_COLORS.toList()
        set.setDrawValues(true)
        val data = PieData(set)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(20f)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data
        pieChart.invalidate()
        pieChart.centerText = "该去完成${closest}了！"
        pieChart.setCenterTextSize(20f)
        pieChart.setUsePercentValues(true)
        val desc = Description()
        desc.text = "DDL接近程度"
        pieChart.description = desc
        val lengend = pieChart.legend
        lengend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
    }
}
