package com.efihumboldt.appligas.ui.activities.DetallePartido

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.efihumboldt.appligas.entidades.Gol
import com.efihumboldt.appligas.R

class GolesAdapter(private val listaGoles: List<Gol>, private val bd: String?) : RecyclerView.Adapter<GolesAdapter.GolesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GolesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_gol, parent, false)
        return GolesViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: GolesViewHolder, position: Int) {

        val gol = listaGoles[position]

        if (gol.lado == "L")
        {
            holder.minutoGolLocal.visibility = VISIBLE
            holder.minutoGolVisita.visibility = INVISIBLE
            holder.pelotaLocal.visibility = VISIBLE
            holder.pelotaVisita.visibility = INVISIBLE
            holder.minutoGolLocal.text = gol.minuto.toString() + "'"

        }
        else
        {
            holder.minutoGolLocal.visibility = INVISIBLE
            holder.minutoGolVisita.visibility = VISIBLE
            holder.pelotaLocal.visibility = INVISIBLE
            holder.pelotaVisita.visibility = VISIBLE
            holder.minutoGolVisita.text = gol.minuto.toString() + "'"
        }

    }

    override fun getItemCount(): Int {
        return listaGoles.size
    }

    inner class GolesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var minutoGolLocal : TextView = view.findViewById(R.id.minutoGolLocal)
        var minutoGolVisita : TextView = view.findViewById(R.id.minutoGolVisita)
        var pelotaLocal : ImageView = view.findViewById(R.id.PelotaLocal)
        var pelotaVisita : ImageView = view. findViewById(R.id.PelotaVisita)


    }
}