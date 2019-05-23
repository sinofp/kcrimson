package top.emptystack.kcrimson

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var selectedFragment: Fragment
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                fab_add.hide()
                selectedFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                fab_add.hide()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                fab_add.show()
                fab_add.onClick {
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
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        fab_add.hide()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
    }
}
