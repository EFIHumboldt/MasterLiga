package com.efihumboldt.appligas.ui.activities.DetalleEquipo

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.R

class UltimosPartidosDetalleEquipoAdapter(private val listaPartidos: List<Partido>, private val bd: String?, private val teamID : Int, private val context: Context) : RecyclerView.Adapter<UltimosPartidosDetalleEquipoAdapter.PartidoViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(partido: Partido)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_partido_detalle_equipo, parent, false)

        val displayMetrics = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels

        val itemWidth = (screenWidth - convertDpToPixel(56f, context)) / 5

        // Establecer el ancho del elemento del RecyclerView
        itemView.layoutParams = ViewGroup.LayoutParams(itemWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)

        return PartidoViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: PartidoViewHolder, position: Int) {
        val posicion = listaPartidos[position]


        holder.resultado.text = posicion.resultadoLocal + " - " + posicion.resultadoVisita

        if (teamID.toString() == posicion.idLocal )
        {
            Glide.with(holder.imgRival.context).load("${bd}/${posicion.imgVisita}").into(holder.imgRival)
            if (posicion.resultadoLocal!! > posicion.resultadoVisita!!) holder.barraResultado.setBackgroundResource(R.color.colorGanado)
            else if (posicion.resultadoLocal < posicion.resultadoVisita.toString()) holder.barraResultado.setBackgroundResource(R.color.colorPerdido)
            else holder.barraResultado.setBackgroundResource(R.color.colorEmpatado)
        }
        else
        {
            Glide.with(holder.imgRival.context).load("${bd}/${posicion.imgLocal}").into(holder.imgRival)
            if (posicion.resultadoVisita!! > posicion.resultadoLocal.toString()) holder.barraResultado.setBackgroundResource(R.color.colorGanado)
            else if (posicion.resultadoVisita < posicion.resultadoLocal.toString()) holder.barraResultado.setBackgroundResource(R.color.colorPerdido)
            else holder.barraResultado.setBackgroundResource(R.color.colorEmpatado)
        }



        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(listaPartidos[position])
        }
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun getItemCount(): Int {
        return listaPartidos.size
    }

    inner class PartidoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imgRival: ImageView = view.findViewById(R.id.escudoRivalDetalleEquipo)
        var resultado: TextView = view.findViewById(R.id.resultadoPartidoDetalleEquipo)
        var barraResultado : View = view.findViewById(R.id.colorResultadoDetalleEquipo)

    }
}