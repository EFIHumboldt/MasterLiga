package com.efihumboldt.appligas.ui.menu_inferior_torneo.config

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.DownloadListener
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.ui.activities.Contactos.ClasificationTableActivity
import com.efihumboldt.appligas.ui.activities.Torneos.TorneoActivity
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class LeagueOptionsAdapter(private val items: List<String>, private val context : Context) :
    RecyclerView.Adapter<LeagueOptionsAdapter.ViewHolder>() {

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
            if (item != "Tabla de clasificación") Toast.makeText(context, "No disponible aún", Toast.LENGTH_SHORT).show()
            else
            {
                if(SharedDataHolder.selectedTournament!!.reglamento == "mostrar")
                {
                    val intent = Intent(context, ClasificationTableActivity::class.java)
                    context.startActivity(intent)
                }
                else  Toast.makeText(context, "No disponible aún", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
