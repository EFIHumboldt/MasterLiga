package com.efihumboldt.appligas.Varios

import androidx.recyclerview.widget.DiffUtil
import com.efihumboldt.appligas.entidades.Torneo

class DiffCallback : DiffUtil.ItemCallback<Torneo>() {
    override fun areItemsTheSame(oldItem: Torneo, newItem: Torneo): Boolean {

        return oldItem.divisionID == newItem.divisionID
    }

    override fun areContentsTheSame(oldItem: Torneo, newItem: Torneo): Boolean {

        return oldItem == newItem
    }
}