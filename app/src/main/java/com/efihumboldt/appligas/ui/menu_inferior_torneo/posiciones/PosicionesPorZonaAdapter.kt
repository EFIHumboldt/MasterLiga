package com.efihumboldt.appligas.ui.menu_inferior_torneo.posiciones

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.R

class PosicionesPorZonaAdapter(
    private val listaPosiciones: List<Posicion>?,
    private val bd: String?,
    private val teamID: Int?,
    private val context: Context,
    private val recyclerViewPosiciones : RecyclerView,
    private val onItemClick: (Posicion) -> Unit
) : RecyclerView.Adapter<PosicionesPorZonaAdapter.PosicionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosicionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_posicion, parent, false)
        return PosicionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PosicionViewHolder, position: Int) {
        val posicion = listaPosiciones?.get(position)

        if (posicion != null) {
            holder.tvNumeroPosicion.text = posicion.numeroPosicion.toString()
            holder.nombreEquipo.text = posicion.nombre
            holder.tvPartidosJugados.text = posicion.partidosJugados.toString()
            holder.tvPartidosGanados.text = posicion.partidosGanados.toString()
            holder.tvPartidosEmpatados.text = posicion.partidosEmpatados.toString()
            holder.tvPartidosPerdidos.text = posicion.partidosPerdidos.toString()
            holder.tvGolesFavor.text = posicion.golesAFavor.toString()
            holder.tvGolesContra.text = posicion.golesEnContra.toString()
            holder.tvDiferenciaGoles.text = posicion.diferenciaDeGoles.toString()
            holder.tvPuntos.text = posicion.puntos.toString()


            //CAMBIAR DESPUES
            //Glide.with(holder.escudoEquipo.context).load("${bd}/${posicion.escudo}").into(holder.escudoEquipo)
            Glide.with(holder.escudoEquipo.context).load(R.drawable.escudo_default).into(holder.escudoEquipo)
            holder.barraPocision.setImageResource(R.drawable.position_bar)

            if (posicion.colorBarra != "NULL") {
                holder.barraPocision.setColorFilter(Color.parseColor(posicion.colorBarra))
            } else {
                holder.barraPocision.visibility = INVISIBLE
            }

            if (posicion.equipo == teamID) holder.background.setBackgroundColor(getColor(context, R.color.grisFullClaro))
        }

    }

    override fun getItemCount(): Int {
        return listaPosiciones?.size ?: 0
    }

    inner class PosicionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvNumeroPosicion: TextView = view.findViewById(R.id.posicion)
        var escudoEquipo: ImageView = view.findViewById(R.id.imgEquipo)
        var nombreEquipo: TextView = view.findViewById(R.id.item_nombre)
        var tvPartidosJugados: TextView = view.findViewById(R.id.item_partidos_jugados)
        var tvPartidosGanados: TextView = view.findViewById(R.id.item_partidos_ganados)
        var tvPartidosEmpatados: TextView = view.findViewById(R.id.item_partidos_empatados)
        var tvPartidosPerdidos: TextView = view.findViewById(R.id.item_partidos_perdidos)
        var tvGolesFavor: TextView = view.findViewById(R.id.item_goles_favor)
        var tvGolesContra: TextView = view.findViewById(R.id.item_goles_contra)
        var tvDiferenciaGoles: TextView = view.findViewById(R.id.item_diferencia_goles)
        var tvPuntos: TextView = view.findViewById(R.id.item_puntos)
        var barraPocision: ImageView = view.findViewById(R.id.barraPosicion)
        var background: CardView = view.findViewById(R.id.card_view_equipos)

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(listaPosiciones!![position])
                }
            }
        }

    }
}
