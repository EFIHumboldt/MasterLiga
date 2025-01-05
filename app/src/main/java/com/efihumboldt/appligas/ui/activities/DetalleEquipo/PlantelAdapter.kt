package com.efihumboldt.appligas.ui.activities.DetalleEquipo

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.entidades.Jugador

class PlantelAdapter(private val listaJugadores: List<Jugador>,
                     private val bd: String?,
                     private val teamID: Int?,
                     private val context: Context,
) : RecyclerView.Adapter<PlantelAdapter.JugadorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_jugador_goles, parent, false)
        return JugadorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {

        val posicion = listaJugadores[position]
        holder.tvDorsal.text = posicion.dorsal
        holder.tvNombre.text = posicion.nombre
        Glide.with(holder.escudoEquipo.context).load(R.drawable.default_profile).into(holder.escudoEquipo)


        //CAMBIAR DESPUES
        //Glide.with(holder.escudoEquipo.context).load("${bd}/${posicion.escudo}").into(holder.escudoEquipo)


        /*
        if (posicion.colorBarra != "NULL") {
            holder.barraPocision.setColorFilter(Color.parseColor(posicion.colorBarra))
        } else {
            holder.barraPocision.visibility = INVISIBLE
        }

         */

    }

    override fun getItemCount(): Int {
        return listaJugadores.size
    }

    inner class JugadorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvDorsal: TextView = view.findViewById(R.id.dorsal)
        var tvNombre : TextView = view.findViewById(R.id.item_nombre)
        var escudoEquipo: ImageView = view.findViewById(R.id.imgJugador)
    }
}