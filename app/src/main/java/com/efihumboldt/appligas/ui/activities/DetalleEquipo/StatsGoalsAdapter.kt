package com.efihumboldt.appligas.ui.activities.DetalleEquipo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.StatsGoal
import com.efihumboldt.appligas.R

class StatsGoalsAdapter(private val stats: StatsGoal, private val bd: String?, private val context: Context) : RecyclerView.Adapter<StatsGoalsAdapter.StatsGoalsViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(partido: Partido)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsGoalsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.frame_stats_detalle_equipo, parent, false)
        return StatsGoalsViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: StatsGoalsViewHolder, position: Int) {
        val posicion = stats



    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class StatsGoalsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var golesHechosLocalTotal : TextView = view.findViewById(R.id.cantGolesLocalTotal)
        var promedioGolesHechosLocal : TextView = view.findViewById(R.id.cantGolesLocalProm)
        var golesHechosVisitaTotal : TextView = view.findViewById(R.id.cantGolesVisitaEnTotal)
        var promedioGolesHechosVisita : TextView = view. findViewById(R.id.cantGolesVisitaProm)
        var golesRecibidosLocalTotal : TextView = view.findViewById(R.id.cantGolesLocalConcebidosTotal)
        var promedioGolesRecibidosLocal : TextView = view.findViewById(R.id.cantGolesLocalConcebidosProm)
        var golesRecibidoVisitasTotal : TextView = view.findViewById(R.id.cantGolesVisitaConcebidosEnTotal)
        var promedioGolesRecibidoVisita : TextView = view.findViewById(R.id.cantGolesVisitaProm)

    }
}