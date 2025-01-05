package com.efihumboldt.appligas.ui.menu_inferior_torneo.config

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.ui.activities.Contactos.ContactosActivity
import com.efihumboldt.appligas.ui.activities.DetalleEquipo.DetalleEquipoActivity

class AppOptionsAdapter(private val items: List<String>, private val context: Context) :
    RecyclerView.Adapter<AppOptionsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewItem: TextView = itemView.findViewById(R.id.textViewItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_accordion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textViewItem.text = item

        holder.itemView.setOnClickListener {
            if (item == "Puntúanos")
            {
                val url =  "https://play.google.com/store/apps/details?id=com.efihumboldt.appligas"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            else if (item == "Contactos")
            {
                val intent = Intent(context, ContactosActivity::class.java)
                context.startActivity(intent)
            }
            else if (item == "Reportar Problema")
            {
                val url =  "https://docs.google.com/forms/d/1aCmA3M0QIoCJHP15xncNwMat8IiZ68OoWWTpuv0ryTE"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
