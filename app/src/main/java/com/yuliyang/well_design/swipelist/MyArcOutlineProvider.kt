package com.yuliyang.well_design.swipelist

import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.ViewOutlineProvider
import org.jetbrains.anko.dip

class MyArcOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(Rect(0, 0, view.width, view.height), view.context.dip(15).toFloat())
    }

}