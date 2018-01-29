package ge.bondx.calories

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.google.firebase.database.FirebaseDatabase
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import ge.bondx.calories.R.color.colorPrimary
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter




class MainActivity : AppCompatActivity() {

    private lateinit var searchView: MaterialSearchView
    private var navigationAdapter: AHBottomNavigationAdapter? = null
    private var tabColors: IntArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "კალორია"
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"))

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        searchView = findViewById<View>(R.id.search_view) as MaterialSearchView

        val bottomNavigation = findViewById<View>(R.id.navigation) as AHBottomNavigation

        //val item1 = AHBottomNavigationItem(R.string.title_home, R.drawable.ic_dashboard_black_24dp, colorPrimary)
        //val item2 = AHBottomNavigationItem(R.string.title_notifications, R.drawable.ic_shopping_cart_black_24dp, colorPrimary)

        //bottomNavigation.addItem(item1)
        //bottomNavigation.addItem(item2)

        tabColors = applicationContext.resources.getIntArray(R.array.tab_colors)
        navigationAdapter = AHBottomNavigationAdapter(this, R.menu.navigation)
        navigationAdapter!!.setupWithBottomNavigation(bottomNavigation, tabColors)

        bottomNavigation.defaultBackgroundColor = resources.getColor(R.color.colorPrimary)
        bottomNavigation.isBehaviorTranslationEnabled = false
        bottomNavigation.accentColor = Color.parseColor("#F63D2B")
        bottomNavigation.inactiveColor = Color.parseColor("#747474")
        bottomNavigation.isForceTint = true
        bottomNavigation.isTranslucentNavigationEnabled = true
        bottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        bottomNavigation.isColored = true
        bottomNavigation.setNotificationBackgroundColor(resources.getColor(R.color.colorBadge))

        bottomNavigation.setOnTabSelectedListener({ position, _ ->
            var selectedFragment: Fragment?
            when (position) {
                0 -> {
                    selectedFragment = MainItemFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, selectedFragment).commit()
                }
                1 -> {
                    selectedFragment = NotificationsFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, selectedFragment).commit()
                }
            }
            true
        })

        bottomNavigation.currentItem = 0
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        val item = menu.findItem(R.id.action_search)
        searchView.setMenuItem(item)
        return true
    }
}