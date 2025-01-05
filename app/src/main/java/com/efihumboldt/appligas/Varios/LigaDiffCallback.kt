package com.efihumboldt.appligas.Varios

import androidx.recyclerview.widget.DiffUtil
import com.efihumboldt.appligas.entidades.Liga

class LigaDiffCallback(private val oldList: List<Liga>, private val newList: List<Liga>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].ID == newList[newItemPosition].ID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}