package com.efihumboldt.appligas.Varios

import androidx.recyclerview.widget.DiffUtil
import com.efihumboldt.appligas.entidades.EquipoSimple

class EquipoDiffCallback(private val oldList: List<EquipoSimple>, private val newList: List<EquipoSimple>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}