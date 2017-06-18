package io.psych.io2017

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import android.widget.Toolbar
import io.psych.io2017.architecture.ViewModelActivity
import io.psych.io2017.downloadablefonts.DownloadableFonts
import io.psych.io2017.dynamicanimation.DynamicAnimationActivity
import io.psych.io2017.dynamicanimation.ReboundActivity
import io.psych.io2017.notifications.NotificationsActivity
import io.psych.io2017.room.RoomActivity

class MainActivity : Activity() {

    val TAG = "MainActivity"

    var constraintLayout : ConstraintLayout? = null
    var menuRoot : View? = null
    var menuItems : MutableList<MenuItem> = ArrayList()
    var menuItemSpacing : Int = 0
    var expandedConstraintSet : ConstraintSet = ConstraintSet()
    var collapsedConstraintSet : ConstraintSet = ConstraintSet()
    var isCollapsed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuItemSpacing = resources.getDimensionPixelSize(R.dimen.collapsable_menu_spacing)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setActionBar(toolbar)

        createMenu()

        collapseMenu(animated = false)
        Handler().postDelayed({
            expandMenu()
        }, 250)
    }

    fun createMenu() {
        constraintLayout = findViewById(R.id.constraintlayout)
        menuRoot = findViewById(R.id.fab)
        menuRoot!!.setOnClickListener {
            toggleMenu()
        }

        addMenuItem(R.id.button_dynamic_animation, R.id.textview_dynamic_animation, View.OnClickListener {
            startActivity(Intent(this, DynamicAnimationActivity::class.java))
        })

        addMenuItem(R.id.button_rebound, R.id.textview_rebound, View.OnClickListener {
            startActivity(Intent(this, ReboundActivity::class.java))
        })

        addMenuItem(R.id.button_viewmodel, R.id.textview_viewmodels, View.OnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        })

        addMenuItem(R.id.button_downloadable_google_font, R.id.textview_google_fonts, View.OnClickListener {
            startActivity(Intent(this, DownloadableFonts::class.java))
        })

        addMenuItem(R.id.button_notifications, R.id.textview_notifications, View.OnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        })

        addMenuItem(R.id.button_room, R.id.textview_room, View.OnClickListener {
            startActivity(Intent(this, RoomActivity::class.java))
        })

        createConstraintSets()
    }

    fun addMenuItem(viewId : Int, titleId : Int, onClick : View.OnClickListener) {
        val menuItem = MenuItem(
                findViewById(viewId),
                findViewById(titleId)
        )
        menuItem.puck.setOnClickListener(onClick)
        menuItems.add(menuItem)
    }

    fun toggleMenu() {
        if (isCollapsed) {
            expandMenu()
        } else {
            collapseMenu()
        }
        isCollapsed = !isCollapsed
    }

    fun createConstraintSets() {
        expandedConstraintSet.clone(constraintLayout)

        collapsedConstraintSet.clone(constraintLayout)
        for (menuItem in menuItems) {
            collapsedConstraintSet.setMargin(menuItem.puck.id, ConstraintSet.BOTTOM, 0)
            collapsedConstraintSet.setAlpha(menuItem.title.id, 0f)
        }
    }

    fun expandMenu(animated: Boolean = true) {
        Log.d(TAG, "expandMenu()")
        val transition = ChangeBounds()
        if (animated) {
            transition.duration = 350
            transition.interpolator = OvershootInterpolator()
        } else {
            transition.duration = 0
        }

        TransitionManager.beginDelayedTransition(constraintLayout, transition)
        expandedConstraintSet.applyTo(constraintLayout)
    }

    fun collapseMenu(animated: Boolean = true) {
        Log.d(TAG, "collapseMenu()")
        val transition = ChangeBounds()
        if (animated) {
            transition.duration = 350
            transition.interpolator = AnticipateInterpolator()
        } else {
            transition.duration = 0
        }

        TransitionManager.beginDelayedTransition(constraintLayout, transition)
        collapsedConstraintSet.applyTo(constraintLayout)
    }

    class MenuItem (val puck:View, val title:TextView) {
    }
}
