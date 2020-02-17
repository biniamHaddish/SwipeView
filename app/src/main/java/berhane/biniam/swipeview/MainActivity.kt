package berhane.biniam.swipeview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import berhane.biniam.swipeview.utils.action
import berhane.biniam.swipeview.utils.snack
import berhane.biniam.swipy.swipe.whenSwipedTo
import kotlinx.android.synthetic.main.activity_main.*
import toastIt

class MainActivity : AppCompatActivity() {

    private var mAdapter: SampleRecyclerViewAdapter? = null

    // Create and set an adapter
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        email_recycler.whenSwipedTo(this) {
            // when view is swiped to left
            left {
                setColor(R.color.md_green)
                setIcon(R.drawable.pin)
                callback {
                    mAdapter!!.notifyDataSetChanged()
                    email_recycler.snack("Pinned ") {
                        action("Undo") {
                            toastIt("UnPinned ")
                        }
                    }
                }
            }
            // View swiped to right
            right {
                setColor(R.color.md_orange)
                setIcon(R.drawable.ic_action_star)
                callback {
                    mAdapter!!.notifyDataSetChanged()
                    email_recycler.snack("Starred ") {
                        action("Undo") {
                            toastIt("UnStarred ")
                        }
                    }

                }
            }
            // when view is swiped to far Right
            longRight {
                setColor(R.color.md_blue)
                setIcon(R.drawable.ic_action_archive)
                callback {
                    val pos = it
                    mAdapter!!.notifyDataSetChanged()
                    email_recycler.snack("Archived ") {
                        action("Undo") {
                            toastIt("UnArchived.")
                        }
                    }
                }
            }
            // when swiped to far Left
            longLeft {
                setColor(R.color.md_red)
                setIcon(R.drawable.ic_action_delete)
                callback {
                    val pos = it
                    mAdapter?.removeItem(it)
                    mAdapter!!.notifyDataSetChanged()
                    email_recycler.snack("Deleted ") {
                        action("Undo") {
                            mAdapter!!.addItem("Row ${pos + 1}", pos)
                        }
                    }
                }
            }
        }

        mAdapter = SampleRecyclerViewAdapter(this)
        email_recycler.adapter = mAdapter
    }
}
