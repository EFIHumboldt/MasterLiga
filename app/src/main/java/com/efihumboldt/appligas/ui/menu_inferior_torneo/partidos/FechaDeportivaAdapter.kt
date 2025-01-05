package com.efihumboldt.appligas.ui.menu_inferior_torneo.partidos

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.ui.activities.DetallePartido.DetallePartidoActivity
import com.efihumboldt.appligas.ui.activities.DetallePartido.DetallePartidoViewModel
import com.efihumboldt.appligas.Varios.CustomLinearLayoutManager
import com.efihumboldt.appligas.Varios.SpaceItemDecoration

class FechaDeportivaAdapter(private val listaFecha: List<FechaDeportiva>, private val bd : String?, private val context: Context,
                            private val recyclerViewFechas: RecyclerView,
                            private val viewModel: DetallePartidoViewModel,
                            private val bannerList : List<Banner>) : RecyclerView.Adapter<FechaDeportivaAdapter.FechaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FechaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_fecha, parent, false)
        return FechaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FechaViewHolder, position: Int) {
        val posicion = listaFecha[position]

        val partidoAdapter = PartidoAdapter(posicion.listaPartido, bd, holder.itemView.context, recyclerViewFechas) { partido ->

            partido.let {viewModel.setSelectedMatch(it) }

            val intent = Intent(holder.itemView.context, DetallePartidoActivity::class.java)
            context.startActivity(intent)
        }
        holder.recyclerViewPartidos.layoutManager = CustomLinearLayoutManager(holder.itemView.context)
        holder.recyclerViewPartidos.adapter = partidoAdapter
        holder.recyclerViewPartidos.addItemDecoration(SpaceItemDecoration(20)) // no anda el traer el dim
        holder.fecha.text = posicion.valor

        if (bannerList.isNotEmpty() && bannerList.size-1 >= position)
        {
            Glide.with(holder.banner.context).load("${bd}/${bannerList[position].link}").into(holder.banner)

        }

    }

    override fun getItemCount(): Int {
        return listaFecha.size
    }


    inner class FechaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerViewPartidos: RecyclerView = view.findViewById(R.id.recyclerViewPartidos)
        val fecha: TextView = view.findViewById(R.id.fecha)
        val banner : ImageView = view.findViewById(R.id.banner2)
    }
}