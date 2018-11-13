package com.jonasdevrient.citypinboard

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Custom item decoration for a vertical [CityListFragment] [RecyclerView]. Adds a
 * small amount of padding to the left of grid items, and a large amount of padding to the right.
 */
class CityGridItemDecoration(private val largePadding: Int, private val smallPadding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = smallPadding
        outRect.right = largePadding
    }
}