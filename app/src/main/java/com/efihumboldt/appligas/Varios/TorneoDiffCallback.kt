package com.efihumboldt.appligas.Varios

import androidx.recyclerview.widget.DiffUtil
import com.efihumboldt.appligas.entidades.Torneo

class TorneoDiffCallback(private val oldList: List<Torneo>, private val newList: List<Torneo>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].divisionID == newList[newItemPosition].divisionID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}