
package berhane.biniam.swipeview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import berhane.biniam.swipy.swipe.whenSwipedTo
import kotlinx.android.synthetic.main.activity_main.*
import toastIt

class MainActivity : AppCompatActivity() {


    private var mAdapter: SampleRecyclerViewAdapter? = null


    // Create and set an adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.whenSwipedTo(this) {
            left {
                setColor(R.color.md_blue)
                setIcon(R.drawable.ic_action_archive)
                callback {
                    mAdapter!!.notifyDataSetChanged()
                    toastIt("Swiped to Left")
                }
            }
            right {
                setColor(R.color.md_red)
                setIcon(R.drawable.ic_action_star)
                callback{
                    mAdapter!!.notifyDataSetChanged()
                    toastIt("Swiped to Right")
                }
            }

            longRight {
                setColor(R.color.md_orange)
                setIcon(R.drawable.ic_action_delete)
                callback {
                    mAdapter!!.removeItem(it)
                    mAdapter!!.notifyDataSetChanged()
                     toastIt("Item Deleted and Removed from the View")
                }
            }
            longLeft {
                setColor(R.color.md_green)
                setIcon(R.drawable.ic_action_unread)
                callback {
                    mAdapter!!.notifyDataSetChanged()
                    toastIt("Swiped  Long Left $it")
                }
            }
        }

        mAdapter = SampleRecyclerViewAdapter(this)
        recyclerView.adapter = mAdapter
    }

}
