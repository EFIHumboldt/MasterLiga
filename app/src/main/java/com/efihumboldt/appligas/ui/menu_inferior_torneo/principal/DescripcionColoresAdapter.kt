package com.efihumboldt.appligas.ui.menu_inferior_torneo.principal

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.efihumboldt.appligas.entidades.ColorDescripcion
import com.efihumboldt.appligas.R

class DescripcionColoresAdapter(private val listaColores: List<ColorDescripcion>, private val bd: String?, private val context: Context) : RecyclerView.Adapter<DescripcionColoresAdapter.ColorDescripcionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorDescripcionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_descripcion_color, parent, false)
        return ColorDescripcionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ColorDescripcionViewHolder, position: Int) {
        val posicion = listaColores[position]

        holder.barraPocision.setImageResource(R.drawable.position_bar)
        if (posicion.color != "NULL") {
            holder.barraPocision.setColorFilter(Color.parseColor(posicion.color));
        } else {
            holder.barraPocision.setColorFilter(Color.parseColor("#FFFFFF"))
        }


        holder.descripcion.text = posicion.descripcion

    }

    override fun getItemCount(): Int {
        return listaColores.size
    }

    inner class ColorDescripcionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var descripcion: TextView  = view.findViewById(R.id.descripcion)
        var barraPocision : ImageView = view.findViewById(R.id.barraPosicion)
        var background : CardView = view.findViewById(R.id.card_view_equipos)

    }
}