package com.efihumboldt.appligas.ui.menu_inferior_torneo.noticias

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.Noticia
import com.efihumboldt.appligas.R

class NoticiasAdapter(private val listaNoticias: List<Noticia>, private val bd : String?, private val context: Context, private val bannerList : List<Banner>) : RecyclerView.Adapter<NoticiasAdapter.NoticiasViewHolder>() {


    private var screenWidth: Int = 0
    private var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(noticia: Noticia)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiasViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_noticia, parent, false)


        return NoticiasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoticiasViewHolder, position: Int) {
        val posicion = listaNoticias[position]

        if (bannerList.isNotEmpty() && bannerList.size-1 >= position % 3 && (((position+1)%3 == 0 && position!= 0) || position == listaNoticias.size -1 ))
        {
            Glide.with(holder.bannerInt.context).load("${bd}/${bannerList[position].link}").into(holder.bannerInt)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(listaNoticias[position])
        }
        holder.bind(posicion)
    }

    override fun getItemCount(): Int {
        return listaNoticias.size
    }

    inner class NoticiasViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imagen: ImageView = view.findViewById(R.id.img_noticia)
        var titulo: TextView = view.findViewById(R.id.titulo_noticia)
        var bannerInt : ImageView = view.findViewById(R.id.banner3)




        init {
            val displayMetrics = DisplayMetrics()
            (context as? Activity)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            screenWidth = displayMetrics.widthPixels
        }

        fun bind(noticia: Noticia) {

            Glide.with(imagen.context).load("${bd}/${noticia.imagen}").into(imagen)
            titulo.text = noticia.titulo

            val layoutParams: ViewGroup.LayoutParams = imagen.layoutParams
            layoutParams.width = screenWidth
            layoutParams.height = (screenWidth * 0.5).toInt()
            imagen.layoutParams = layoutParams


        }

    }
}