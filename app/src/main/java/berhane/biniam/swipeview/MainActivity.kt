package berhane.biniam.swipeview

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import berhane.biniam.swipeview.swipe.whenSwipedTo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private var mAdapter: SampleRecyclerViewAdapter? = null


    // Create and set an adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        /**
         * Some Swipe Impl
         */
        recyclerView.whenSwipedTo {

            left {
                color = ContextCompat.getColor(this@MainActivity, R.color.md_blue)
                icon = getDrawableInt(R.drawable.ic_action_archive)
                iconMargin = 35
                callback = {
                    mAdapter!!.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Swiped to Left", Toast.LENGTH_LONG).show()
                }
            }
            right {
                color = ContextCompat.getColor(this@MainActivity, R.color.md_red)
                icon = getDrawableInt(R.drawable.ic_action_star)
                iconMargin = 35
                callback = {
                    mAdapter!!.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Swiped to Right", Toast.LENGTH_LONG).show()
                }
            }
            longRight {
                color = ContextCompat.getColor(this@MainActivity, R.color.md_orange)
                icon = getDrawableInt(R.drawable.ic_action_delete)
                iconMargin = 35
                callback = {
                    mAdapter!!.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Swiped Long Right", Toast.LENGTH_LONG).show()
                }
            }
            longLeft {
                color = ContextCompat.getColor(this@MainActivity, R.color.md_green)
                icon = getDrawableInt(R.drawable.ic_action_unread)
                iconMargin = 35
                callback = {
                    mAdapter!!.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Swiped  Long Left", Toast.LENGTH_LONG).show()
                }
            }

        }

        mAdapter = SampleRecyclerViewAdapter(this)
        recyclerView.adapter = mAdapter
    }





    private fun getDrawableInt(@DrawableRes resDrw: Int? = null, drawable: Drawable? = null): Drawable? {
        return drawable ?: ContextCompat.getDrawable(this, resDrw!!)

    }
}
