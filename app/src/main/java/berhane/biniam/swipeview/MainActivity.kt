package berhane.biniam.swipeview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import berhane.biniam.swipeview.utils.action
import berhane.biniam.swipeview.utils.snack
import berhane.biniam.swipy.swipe.whenSwipedTo
import com.google.android.material.snackbar.Snackbar
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
                setColor(R.color.md_green)
                setIcon(R.drawable.pin)
                callback {
                    mAdapter!!.notifyDataSetChanged()
                    recyclerView.snack("Pinned ") {
                        action("Undo") {
                            toastIt("UnPinned ")
                        }
                    }
                }
            }
            right {
                setColor(R.color.md_orange)
                setIcon(R.drawable.ic_action_star)
                callback {
                    mAdapter!!.notifyDataSetChanged()
                    recyclerView.snack("Starred ") {
                        action("Undo") {
                            toastIt("UnStarred ")
                        }
                    }

                }
            }

            longRight {
                setColor(R.color.md_blue)
                setIcon(R.drawable.ic_action_archive)
                callback {
                    val pos = it
                    mAdapter!!.notifyDataSetChanged()
                    recyclerView.snack("Archived ") {
                        action("Undo") {
                            toastIt("UnArchived.")
                        }
                    }
                }
            }
            longLeft {
                setColor(R.color.md_red)
                setIcon(R.drawable.ic_action_delete)
                callback {
                    val pos = it
                    mAdapter!!.removeItem(it)
                    mAdapter!!.notifyDataSetChanged()
                    recyclerView.snack("Deleted ") {
                        action("Undo") {
                            mAdapter!!.addItem("Row ${pos + 1}", pos)
                        }
                    }
                }
            }
        }

        mAdapter = SampleRecyclerViewAdapter(this)
        recyclerView.adapter = mAdapter
    }
}
