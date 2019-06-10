package berhane.biniam.swipeview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import berhane.biniam.swipeview.R.drawable.*
import berhane.biniam.swipeview.swipe.swipe
import kotlinx.android.synthetic.main.activity_main.*
import berhane.biniam.swipeview.R.drawable.ic_email_24dp as ic_email_24dp1
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import berhane.biniam.swipeview.swipe.recyclerViewSwipe


class MainActivity : AppCompatActivity() {


    private var mAdapter: SampleRecyclerViewAdapter? = null


    // Create and set an adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))



         recyclerView.swipe{

                left {
                    color = ContextCompat.getColor(this@MainActivity, R.color.icon_tint_selected)
                    icon = getDrawableInt(R.drawable.ic_email_24dp)
                    iconMargin = 20
                    action = {
                        // it is the integer value or the position for the adapter
                        mAdapter!!.removeItem(it)
                        mAdapter!!.notifyDataSetChanged()
                        Toast.makeText(this@MainActivity,"Swiped to Left",Toast.LENGTH_LONG).show()
                    }
                }
                right {
                    color = ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
                    icon = getDrawableInt(ic_more_horiz_24dp)
                    iconMargin = 20
                    action = {
                        mAdapter!!.notifyDataSetChanged()
                        Toast.makeText(this@MainActivity,"Swiped to Right",Toast.LENGTH_LONG).show()
                    }
                }
            }
        mAdapter = SampleRecyclerViewAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun getDrawableInt(@DrawableRes resDrw: Int? = null, drawable: Drawable? = null): Drawable? {
        return drawable ?: ContextCompat.getDrawable(this, resDrw!!)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            if (item.itemId == R.id.actionRefresh) {
                mAdapter!!.reloadItems()
                mAdapter!!.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", e.message)
        }

        return super.onOptionsItemSelected(item)
    }
}
