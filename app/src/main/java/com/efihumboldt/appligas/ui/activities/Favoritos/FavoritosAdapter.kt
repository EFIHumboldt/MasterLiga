package com.efihumboldt.appligas.ui.activities.Favoritos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.EquipoDiffCallback

class FavoritosAdapter(private var listaEquipos: List<EquipoSimple>, private val bd: String?, private val context: Context) : RecyclerView.Adapter<FavoritosAdapter.EquiposViewHolder>() {

    private var itemWidth: Int = 0
    private var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(equipo: EquipoSimple)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }


    fun setItemWidth(width: Int) {
        this.itemWidth = width
        notifyDataSetChanged() // Notificar cambios para que se vuelva a dibujar la lista
    }



    fun submitList(newList: List<EquipoSimple>) {
        val diffCallback = EquipoDiffCallback(listaEquipos, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listaEquipos = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquiposViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_equipo, parent, false)
        val layoutParams = itemView.layoutParams
        layoutParams.width = itemWidth
        itemView.layoutParams = layoutParams

        return EquiposViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EquiposViewHolder, position: Int) {
        val posicion = listaEquipos[position]
        holder.tvNombreEquipo.text = posicion.nombrecorto
        Glide.with(holder.escudoEquipo.context).load("${bd}/${posicion.escudo}").into(holder.escudoEquipo)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(listaEquipos[position])
        }
    }

    override fun getItemCount(): Int {
        return listaEquipos.size
    }

    inner class EquiposViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvNombreEquipo: TextView = view.findViewById(R.id.nombreEquipo)
        var escudoEquipo: ImageView = view.findViewById(R.id.imgEquipo)

    }
}