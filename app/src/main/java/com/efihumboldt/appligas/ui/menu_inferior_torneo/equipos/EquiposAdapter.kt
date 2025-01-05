package com.efihumboldt.appligas.ui.menu_inferior_torneo.equipos

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
import com.efihumboldt.appligas.Varios.SharedPreferencesManager

class EquiposAdapter(private var listaEquipos: List<EquipoSimple>, private val bd: String?, private val context: Context) : RecyclerView.Adapter<EquiposAdapter.EquiposViewHolder>() {

    private var itemWidth: Int = 0
    private var onItemClickListener: OnItemClickListener? = null
    private val sharedPreferencesManager = SharedPreferencesManager(context)
    interface OnItemClickListener {
        fun onItemClick(equipo: EquipoSimple)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun setItemWidth(width: Int) {
        this.itemWidth = width
        notifyDataSetChanged()
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
        //Glide.with(holder.escudoEquipo.context).load("${bd}/${posicion.escudo}").into(holder.escudoEquipo)
        Glide.with(holder.escudoEquipo.context).load(R.drawable.escudo_default).into(holder.escudoEquipo)
        holder.selected.setImageResource(if (sharedPreferencesManager.isFavorite(posicion)) R.drawable.selected_star else R.drawable.unselected_star)
        holder.selected.setColorFilter(R.color.logos)

        holder.selected.setOnClickListener {
            if (sharedPreferencesManager.isFavorite(posicion)) {
                sharedPreferencesManager.removeFavorite(posicion)
                holder.selected.setImageResource(R.drawable.unselected_star)
            } else {
                sharedPreferencesManager.addFavorite(posicion)
                holder.selected.setImageResource(R.drawable.selected_star)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(posicion)
        }


    }

    override fun getItemCount(): Int {
        return listaEquipos.size
    }

    inner class EquiposViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvNombreEquipo: TextView = view.findViewById(R.id.nombreEquipo)
        var escudoEquipo: ImageView = view.findViewById(R.id.imgEquipo)
        var selected : ImageView = view.findViewById(R.id.selected)

    }
}