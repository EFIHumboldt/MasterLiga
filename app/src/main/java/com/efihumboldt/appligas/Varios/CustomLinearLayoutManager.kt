package com.efihumboldt.appligas.Varios

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class CustomLinearLayoutManager(context : Context) : LinearLayoutManager(context) {

    override fun canScrollVertically(): Boolean {
        return false
    }
}