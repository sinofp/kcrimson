package top.emptystack.kcrimson

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_new_event.*
import kotlinx.android.synthetic.main.content_new_event.*
import org.jetbrains.anko.db.insert
import java.util.*

class NewEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event)
        setSupportActionBar(toolbar)

        val c = Calendar.getInstance()

        datePicker.minDate = c.timeInMillis
        datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
//            toast("$year/${monthOfYear+1}/$dayOfMonth")
            // 在recyclerview中计算当前时间时，会把hourOfDay、minute、second都赋0，但还有最后的微秒之类的，
            // 这里如果给它们都赋0的话，可能会因为存储时微秒比读取时微秒小一点点而造成相减后整除86400000的误差
            // 比如1558656000420-1558569600821/86400000=0，但其实得0.9999953587962963
            c.set(year, monthOfYear, dayOfMonth, 1, 0, 0)
        }

        fab.setOnClickListener { view ->
            if (c.get(Calendar.DATE) == Calendar.getInstance().get(Calendar.DATE)) {
                Snackbar.make(view, "今日事今日毕！", Snackbar.LENGTH_SHORT).show()
            } else {
                if (todo_name.text.toString().isBlank()) {
                    Snackbar.make(view, "请输入事件名！", Snackbar.LENGTH_SHORT).show()
                } else {
                    val name = todo_name.text.toString()
                    val ddl = c.timeInMillis // .toInt()
                    Log.d("sql", "name: $name, ddl: $ddl")
                    database.use {
                        insert("Todo", "name" to name, "ddl" to ddl)
                    }
                    this.finish()
                }
            }
        }
    }
}
