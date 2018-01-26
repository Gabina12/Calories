package ge.bondx.calories

import android.graphics.Color
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.database.FirebaseDatabase
import com.miguelcatalan.materialsearchview.MaterialSearchView
import ge.bondx.calories.database.MyDBHandler
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    internal lateinit var searchView: MaterialSearchView

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment?
        when (item.itemId) {
            R.id.navigation_home -> {
                selectedFragment = MainItemFragment.newInstance(1)
                supportFragmentManager.beginTransaction().replace(R.id.main_container, selectedFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                selectedFragment = NotificationsFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.main_container, selectedFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "კალორია"
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"))

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        searchView = findViewById<View>(R.id.search_view) as MaterialSearchView

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        val item = menu.findItem(R.id.action_search)
        searchView.setMenuItem(item)
        return true
    }
}
