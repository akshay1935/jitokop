package `in`.ashvasoft.jitokop

import `in`.adityaitsolutions.gst.Helpers.SharedPrefManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import android.widget.TextView
import android.content.Intent
import android.support.v7.app.AlertDialog

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var sm: SharedPrefManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "JITO-Kolhapur"
        sm = SharedPrefManager(this)

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val navName = headerView.findViewById(R.id.tvName) as TextView
        val navMobile = headerView.findViewById(R.id.tvMobile) as TextView
        navName.text = sm!!.getName()
        navMobile.text = sm!!.getMobile()
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_logout -> {
                val builder = AlertDialog.Builder(this@HomeActivity)
                builder.setTitle("Confirmation!")
                builder.setMessage("Do you want to logout?")
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    sm!!.getInstance(this@HomeActivity).logout()
                    val intent = Intent(this,LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }
                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                }
                builder.show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
