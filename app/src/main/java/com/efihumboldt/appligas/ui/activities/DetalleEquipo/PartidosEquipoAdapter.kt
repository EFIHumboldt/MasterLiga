package com.efihumboldt.appligas.ui.activities.DetalleEquipo

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.Partido
import java.util.Calendar

class PartidosEquipoAdapter(private val listaPartido: List<Partido>,
                            private val bd: String?) : RecyclerView.Adapter<PartidosEquipoAdapter.PartidoViewHolder>() {


    val horaActual = Calendar.getInstance().time
    var usados = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_partido_equipo_ii, parent, false) //CAMBIAR por las dudas antes estaba una que decia card_view_partido_equipo
        return PartidoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PartidoViewHolder, position: Int) {
        val posicion = listaPartido[position]


        //CAMBIAR DESPUES
        //Glide.with(holder.imgLocal.context).load("${bd}/${posicion.imgLocal}").into(holder.imgLocal)
        //Glide.with(holder.imgVisita.context).load("${bd}/${posicion.imgVisita}").into(holder.imgVisita)
        Glide.with(holder.imgLocal.context).load(R.drawable.escudo_default).into(holder.imgLocal)
        Glide.with(holder.imgVisita.context).load(R.drawable.escudo_default).into(holder.imgVisita)

        holder.nombreLocal.text = posicion.nombreLocal
        holder.nombreVisita.text = posicion.nombreVisita
        holder.resultadoLocal.text = posicion.resultadoLocal
        holder.resultadoVisita.text = posicion.resultadoVisita
        holder.estadoPartido.text = posicion.infoPartido

        if (posicion.finSegundoTiempo == null && posicion.inicioPrimerTiempo != null)
        {
            holder.estadoPartido.apply {
                typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold)
                setTextColor(ContextCompat.getColor(context, R.color.verdeMatch))
            }
        }
        if ((posicion.resultadoLocal == "0" && posicion.resultadoVisita == "0") || (posicion.resultadoLocal == null && posicion.resultadoVisita == null)) //es porque empataron sin goles
        {
            holder.localBall.visibility = View.INVISIBLE
            holder.visitBall.visibility = View.INVISIBLE
        }
        else if (posicion.goles.size > 0){
            for (i in posicion.goles.indices) {
                if (posicion.goles[i].lado == "L") {

                    var textoActual = holder.localGoals.text.toString()

                    if (textoActual == "") {
                        holder.localGoals.text = "${posicion.goles[i].jugador}"
                    }else {
                        holder.localGoals.text = "${textoActual}, ${posicion.goles[i].jugador}"
                    }
                }
                else {
                    var textoActual = holder.visitGoals.text.toString()

                    if (textoActual == "") {
                        holder.localGoals.text = "${posicion.goles[i].jugador}"
                    }else {
                        holder.localGoals.text = "${textoActual}, ${posicion.goles[i].jugador}"
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listaPartido.size
    }

    inner class PartidoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imgLocal: ImageView = view.findViewById(R.id.imgLocal)
        var imgVisita: ImageView = view.findViewById(R.id.imgVisita)
        var nombreLocal: TextView = view.findViewById(R.id.tvNombreLocal)
        var nombreVisita: TextView = view.findViewById(R.id.tvNombreVisita)
        var resultadoLocal: TextView = view.findViewById(R.id.tvResultadoLocal)
        var resultadoVisita: TextView = view.findViewById(R.id.tvResultadoVisita)
        var estadoPartido: TextView = view.findViewById(R.id.tvEstadoPartido)
        var localBall : ImageView = view.findViewById(R.id.left_ball)
        var localGoals : TextView = view.findViewById(R.id.left_goals)
        var visitBall : ImageView = view.findViewById(R.id.right_ball)
        var visitGoals : TextView = view.findViewById(R.id.right_goals)

    }
}