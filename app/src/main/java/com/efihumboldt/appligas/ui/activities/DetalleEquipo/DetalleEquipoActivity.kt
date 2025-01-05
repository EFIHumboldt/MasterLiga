package com.efihumboldt.appligas.ui.activities.DetalleEquipo

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.BannerDAOImpl
import com.efihumboldt.appligas.implementaciones.EquipoSimpleDAOImpl
import com.efihumboldt.appligas.implementaciones.PartidoDAOImpl
import com.efihumboldt.appligas.implementaciones.PosicionDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import com.efihumboldt.appligas.interfaces.EquipoSimpleDAO
import com.efihumboldt.appligas.interfaces.PartidoDAO
import com.efihumboldt.appligas.interfaces.PosicionDAO
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.CustomTypefaceSpan
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.services.BannerService
import com.efihumboldt.appligas.services.EquipoSimpleService
import com.efihumboldt.appligas.services.PartidoService
import com.efihumboldt.appligas.services.PosicionService
import com.efihumboldt.appligas.ui.activities.DetallePartido.DetallePartidoActivity
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import com.efihumboldt.appligas.Varios.SharedDataHolder.bd
import com.efihumboldt.appligas.Varios.SharedDataHolder.selectedTournament
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.ActivityDetalleEquipoBinding
import com.efihumboldt.appligas.databinding.CustomActionBarBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class DetalleEquipoActivity : AppCompatActivity() {

    private lateinit var viewModel: DetalleEquipoViewModel
    private lateinit var recyclerViewTablaDetalleEquipo : RecyclerView
    private lateinit var recyclerViewLastMatchs : RecyclerView
    private lateinit var headerLayout : RelativeLayout
    private lateinit var binding: ActivityDetalleEquipoBinding
    private lateinit var viewModelTorneo : DetalleTorneoViewModel
    private lateinit var customHeader: CustomActionBarBinding
    private lateinit var customToolbar : Toolbar
    private lateinit var swipeRefreshLayoutActivityEquipo : SwipeRefreshLayout
    private var cargaDatosJob: Job? = null
    private lateinit var bannerList : List<Banner>



    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_equipo)

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(this)



        viewModel = ViewModelProvider(this)[DetalleEquipoViewModel::class.java]
        viewModelTorneo = ViewModelProvider(this)[DetalleTorneoViewModel::class.java]

        binding = ActivityDetalleEquipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        headerLayout = binding.headerLayout

        val team = viewModel.selectedTeam

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        setupViewPager(viewPager)

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)



        if (team != null)
        {


            tabLayout.setTabTextColors(Color.parseColor("#101010"), Color.parseColor(team.color))
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(team.color))

            setHeaderLayout(team, selectedTournament!!.nombreTorneoDivision)
        }

        val typeface = ResourcesCompat.getFont(this, R.font.montserrat_regular)
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                val spannable = SpannableString(tab.text)
                spannable.setSpan(StyleSpan(Typeface.BOLD), 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannable.setSpan(CustomTypefaceSpan("", typeface!!), 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tab.text = spannable
            }
        }


    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(Fragment1(), "General")
        adapter.addFragment(Fragment2(), "Partidos")
        viewPager.adapter = adapter
    }

    private fun setHeaderLayout(team: EquipoSimple, nombre : String)
    {
        val teamColor = Color.parseColor(team.color)
        headerLayout.setBackgroundColor(teamColor)

        val imageHeaderTeam : ImageView = binding.frameHeader.customActionBarIcon
        val tittleHeaderTeam : TextView = binding.frameHeader.customActionBarTitle
        val subtitleHeaderTeam : TextView = binding.frameHeader.customActionBarSubtitle

        //CAMBIAR DESPUES
        //Glide.with(imageHeaderTeam.context).load("${viewModel.bd}/${team.escudo}").into(imageHeaderTeam)
        Glide.with(imageHeaderTeam.context).load(R.drawable.escudo_default).into(imageHeaderTeam)
        tittleHeaderTeam.text = team.nombrecompleto

        val partes = nombre.split("-")
        //CAMBIAR DESPUES
        //subtitleHeaderTeam.text = partes[1]

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = teamColor
        supportActionBar?.setBackgroundDrawable(ColorDrawable(teamColor))


        val customActionBar = layoutInflater.inflate(R.layout.custom_action_bar_layout_detalle_equipo, null)
        customToolbar = customActionBar.findViewById(R.id.custom_toolbar_detalle_equipo)
        customToolbar.setBackgroundDrawable(ColorDrawable(teamColor))
        val customTitle : TextView = customActionBar.findViewById(R.id.customActionBarTitle)
        customTitle.text = "Detalles de ${team.nombrecorto}"


        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            customView = customActionBar
            setCustomView(customActionBar)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cargaDatosJob?.cancel()
    }

}