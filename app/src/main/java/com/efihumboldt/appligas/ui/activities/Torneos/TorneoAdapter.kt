package com.efihumboldt.appligas.ui.activities.Torneos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.Varios.TorneoDiffCallback


class TorneoAdapter(private var listaTorneos: List<Torneo>, private val fotoTorneo: String?, private val context: Context) :
    RecyclerView.Adapter<TorneoAdapter.TorneoViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(torneo: Torneo)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun submitList(newList: List<Torneo>) {
        val diffCallback = TorneoDiffCallback(listaTorneos, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listaTorneos = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorneoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_torneo, parent, false)
        return TorneoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TorneoViewHolder, position: Int) {
        val posicion = listaTorneos[position]

        Glide.with(holder.imgTorneo.context).load("${SharedDataHolder.selectedLeague?.link}/${SharedDataHolder.selectedLeague?.logo}").into(holder.imgTorneo)
        holder.nombreTorneo.text = posicion.nombreTorneoDivision.toString()

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(listaTorneos[position])
        }
    }

    override fun getItemCount(): Int {
        return listaTorneos.size
    }

    inner class TorneoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imgTorneo: ImageView = view.findViewById(R.id.img_torneo)
        var nombreTorneo: TextView  = view.findViewById(R.id.nombre_torneo)

    }
}