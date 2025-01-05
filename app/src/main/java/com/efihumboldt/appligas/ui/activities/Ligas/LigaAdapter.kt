package com.efihumboldt.appligas.ui.activities.Ligas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.LigaDiffCallback

class LigaAdapter(private var listaLigas: List<Liga>, private val context: Context) : RecyclerView.Adapter<LigaAdapter.LigaViewHolder>() {

    private var onItemClickListener: LigaAdapter.OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(liga: Liga)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun submitList(newList: List<Liga>) {
        val diffCallback = LigaDiffCallback(listaLigas, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listaLigas = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LigaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_liga, parent, false)
        return LigaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LigaViewHolder, position: Int) {
        val posicion = listaLigas[position]


        holder.nombreLiga.text = posicion.nombre
        Glide.with(holder.imgLiga.context).load(R.drawable.escudo_default).into(holder.imgLiga)
        //Glide.with(holder.imgLiga.context).load("${posicion.link}/${posicion.logo}").into(holder.imgLiga)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(listaLigas[position])
        }
    }

    override fun getItemCount(): Int {
        return listaLigas.size
    }

    inner class LigaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imgLiga: ImageView = view.findViewById(R.id.img_liga)
        var nombreLiga: TextView = view.findViewById(R.id.nombre_liga)

    }
}