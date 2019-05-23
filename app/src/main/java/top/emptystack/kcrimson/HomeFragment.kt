package top.emptystack.kcrimson

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.PieChart
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//    private var listener: OnFragmentInteractionListener? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        arguments?.let {
////            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
////        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val pieChart = context.findViewById(R.id.pieChart)
//        pieChart.setUsePercentValues(true)
        // Inflate the layout for this fragment
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
//    // TODO: Rename method, update argument and hook method into UI event
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
//    interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment HomeFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            HomeFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}
