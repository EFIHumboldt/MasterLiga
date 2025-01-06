package com.efihumboldt.appligas.ui.menu_inferior_torneo.principal

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.BannerDAOImpl
import com.efihumboldt.appligas.implementaciones.PosicionDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import com.efihumboldt.appligas.interfaces.PosicionDAO
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.services.BannerService
import com.efihumboldt.appligas.services.PosicionService
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import com.efihumboldt.appligas.databinding.FragmentPrincipalBinding
import com.efihumboldt.appligas.ui.menu_inferior_torneo.principal.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.log

class PrincipalFragment : Fragment() {

    private var _binding: FragmentPrincipalBinding? = null
    private lateinit var viewModel: DetalleTorneoViewModel
    private lateinit var tablaPosiciones: List<Posicion>
    private lateinit var bd: String
    private lateinit var swipeRefreshLayoutFragmentPosiciones: SwipeRefreshLayout
    private lateinit var recyclerViewPosiciones: RecyclerView
    private lateinit var recyclerViewDescripcionColores: RecyclerView
    private var cargaDatosJob: Job? = null
    private var cargaDatosJob2: Job? = null
    private lateinit var bannerList: List<Banner>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(requireContext())

        _binding = FragmentPrincipalBinding.inflate(inflater, container, false)

        val viewPager: ViewPager = binding.viewPagerPrincipal
        val tabLayout: TabLayout = binding.tabLayoutPrincipal
        tabLayout.setTabTextColors(Color.parseColor("#101010"), Color.parseColor("#000000"))
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000")) //CAMBIAR por color del torneo, como arriba
        setupViewPager(viewPager, tabLayout)




        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                Log.e("hola",tab.position.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cargaDatosJob?.cancel()
        cargaDatosJob2?.cancel()
        _binding = null
    }


    private fun setupViewPager(viewPager: ViewPager, tabLayout: TabLayout) {

        val adapter = ViewPagerAdapter(childFragmentManager)

        adapter.addFragment(PrincipalPrimeroFragment(), "POSICIONES")
        adapter.addFragment(PrincipalSegundoFragment(), "ESTADÍSTICAS")
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                Log.e("ViewPager", "Page selected: $position")
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

        tabLayout.setupWithViewPager(viewPager)
    }

}