package top.emptystack.kcrimson

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

//    private lateinit var textMessage: TextView
    private lateinit var selectedFragment: Fragment
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
//                textMessage.setText(R.string.title_home)
                fab_add.hide()
                selectedFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
//                textMessage.setText(R.string.title_dashboard)
                fab_add.hide()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
//                textMessage.setText(R.string.title_notifications)
                fab_add.show()
                fab_add.onClick {
//                    toast("you clicked!")
                    startActivity<NewEventActivity>()
                }
                selectedFragment = EventFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        fab_add.hide()

//        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
    }
}
