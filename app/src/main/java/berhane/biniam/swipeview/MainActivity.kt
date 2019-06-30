
package berhane.biniam.swipeview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import berhane.biniam.swipy.swipe.whenSwipedTo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private var mAdapter: SampleRecyclerViewAdapter? = null


    // Create and set an adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        /**
         * Some Swipe Impl
         *
         */
        recyclerView.whenSwipedTo(this) {
            left {
                setColor(R.color.md_blue)
                setIcon(R.drawable.ic_action_archive)
                callback {
                    mAdapter!!.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Swiped to Left", Toast.LENGTH_LONG).show()
                }
            }

            right {
                setColor(R.color.md_red)
                setIcon(R.drawable.ic_action_star)
                callback{
                    mAdapter!!.notifyDataSetChanged()
                  Toast.makeText(this@MainActivity, "Swiped to Right", Toast.LENGTH_LONG).show()
                }
            }

            longRight {
                setColor(R.color.md_orange)
                setIcon(R.drawable.ic_action_delete)
                callback {
                    mAdapter!!.removeItem(it)
                    mAdapter!!.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Item Deleted and Removed from the View $it", Toast.LENGTH_LONG)
                        .show()
                }
            }

            longLeft {
                setColor(R.color.md_green)
                setIcon(R.drawable.ic_action_unread)
                callback {
                    mAdapter!!.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Swiped  Long Left $it", Toast.LENGTH_LONG).show()
                }
            }
        }

        mAdapter = SampleRecyclerViewAdapter(this)
        recyclerView.adapter = mAdapter
    }

}
