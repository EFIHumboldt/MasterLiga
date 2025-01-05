package com.efihumboldt.appligas.ui.menu_inferior_torneo.equipos

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.ui.activities.DetalleEquipo.DetalleEquipoActivity
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.BannerDAOImpl
import com.efihumboldt.appligas.implementaciones.EquipoSimpleDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import com.efihumboldt.appligas.interfaces.EquipoSimpleDAO
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.services.BannerService
import com.efihumboldt.appligas.services.EquipoSimpleService
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import com.efihumboldt.appligas.Varios.SharedDataHolder.selectedTournament
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.FragmentEquiposBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class EquiposFragment : Fragment() {

    private var _binding: FragmentEquiposBinding? = null
    private lateinit var recyclerViewEquipos: RecyclerView
    private lateinit var viewModel: DetalleTorneoViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var listaPosiciones: List<EquipoSimple> = emptyList()
    private lateinit var equiposAdapter : EquiposAdapter
    private lateinit var editTextSearch : EditText
    private var cargaDatosJob: Job? = null
    private var cargaDatosJob2: Job? = null
    private lateinit var bannerList : List<Banner>



    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEquiposBinding.inflate(inflater, container, false)

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(requireContext())

        val root: View = binding.root
        editTextSearch = binding.editTextSearch
        editTextSearch.isEnabled = false

        recyclerViewEquipos = binding.recyclerViewEquipos
        swipeRefreshLayout = binding.swipeRefreshLayoutFragmentEquipos


        // Configuración del RecyclerView con un GridLayoutManager de 3 columnas
        val layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerViewEquipos.layoutManager = layoutManager


        viewModel = ViewModelProvider(requireActivity())[DetalleTorneoViewModel::class.java]
        val selectedTournament = viewModel.torneoSeleccionado
        val bd = viewModel.bd

        val apiService: ApiService = ApiServiceImpl(requireContext(), bd)
        val equipoSimpleDAO: EquipoSimpleDAO = EquipoSimpleDAOImpl(apiService.equipoSimpleApiService, bd, selectedTournament!!.divisionID, requireContext())
        val equipoSimpleService = EquipoSimpleService(equipoSimpleDAO)


        val screenWidth = resources.displayMetrics.widthPixels
        val margin = resources.getDimensionPixelSize(R.dimen.card_equipo_margin)
        val marginExterno = resources.getDimensionPixelSize(R.dimen.recyler_margin)
        val itemWidth = (screenWidth - (layoutManager.spanCount - 1) * margin - marginExterno * 4) / layoutManager.spanCount

       cargaDatosJob2 = viewLifecycleOwner.lifecycleScope.launch {
           coroutineScope {
               binding.progressBar.visibility = View.VISIBLE
               cargarDatos(equipoSimpleService, itemWidth, bd, 0)

               editTextSearch.addTextChangedListener(object : TextWatcher {
                   override fun afterTextChanged(s: Editable?) {
                       // No es necesario implementar este método
                   }

                   override fun beforeTextChanged(
                       s: CharSequence?,
                       start: Int,
                       count: Int,
                       after: Int
                   ) {
                       // No es necesario implementar este método
                   }

                   override fun onTextChanged(
                       s: CharSequence?,
                       start: Int,
                       before: Int,
                       count: Int
                   ) {

                       val filteredLigas = listaPosiciones.filter { equipo ->
                           equipo.nombrecompleto.contains(s.toString(), ignoreCase = true)
                       }

                       equiposAdapter.submitList(filteredLigas)
                   }
               })
           }
        }

        swipeRefreshLayout.setOnRefreshListener {

            cargaDatosJob?.cancel()
            cargaDatosJob2?.cancel()

            cargaDatosJob = viewLifecycleOwner.lifecycleScope.launch {

                try {
                    coroutineScope {
                        if (isAdded && !isRemoving) {

                            binding.progressBar.visibility = View.VISIBLE
                            cargarBanner(bd)
                            cargarDatos(equipoSimpleService, itemWidth, bd, 2000)
                        }
                    }
                } catch (e : CancellationException){

                }
                }
            }

        val searchIconDrawable = editTextSearch.compoundDrawablesRelative[2] // El índice 2 representa el drawableEnd
        searchIconDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.lupa), PorterDuff.Mode.SRC_IN)
        editTextSearch.background.setColorFilter(Color.parseColor(selectedTournament?.color), PorterDuff.Mode.SRC_IN)

        return root
    }

    private fun abrirActividadConItem(selectedTeam : EquipoSimple) {

        val intent = Intent(requireContext(), DetalleEquipoActivity::class.java)
        selectedTeam.let{viewModel.setSelectedTeam(it)}
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        cargaDatosJob?.cancel()
        cargaDatosJob2?.cancel()
        _binding = null
    }

    private suspend fun cargarDatos(equipoSimpleService: EquipoSimpleService, itemWidth: Int, bd: String?, delay: Long) {
            val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
            setInteraccionUsuarioHabilitada(navView, false)

            try {
                listaPosiciones = equipoSimpleService.getAllEquipos()
                withContext(Dispatchers.Main) {

                    if (isActive) {
                        cargarBanner(bd)

                        equiposAdapter = EquiposAdapter(listaPosiciones, bd, requireActivity())
                        recyclerViewEquipos.adapter = equiposAdapter

                        equiposAdapter.setOnItemClickListener(object : EquiposAdapter.OnItemClickListener {
                            override fun onItemClick(equipo: EquipoSimple) {
                                abrirActividadConItem(equipo)
                            }
                        })
                        equiposAdapter.setItemWidth(itemWidth)
                        binding.progressBar.visibility = View.GONE
                        editTextSearch.isEnabled = true
                        setInteraccionUsuarioHabilitada(navView, true)
                        swipeRefreshLayout.isRefreshing = false
                    }


                }
            } catch (e: HttpException) {
                //e.printStackTrace()
                e.response()?.code()
                setInteraccionUsuarioHabilitada(navView, true)
                swipeRefreshLayout.isRefreshing = false
            } catch (e: IOException) {
                e.printStackTrace()
                setInteraccionUsuarioHabilitada(navView, true)
                swipeRefreshLayout.isRefreshing = false
            }

    }

    private fun setInteraccionUsuarioHabilitada(navigationView: BottomNavigationView, habilitado: Boolean) {
        for (i in 0 until navigationView.menu.size()) {
            navigationView.menu.getItem(i).isEnabled = habilitado
        }
    }

    private suspend fun cargarBanner(bd : String?){

        val apiService: ApiService = ApiServiceImpl(requireContext(), bd)
        val bannerDAO: BannerDAO = BannerDAOImpl(apiService.bannerApiService, bd, selectedTournament!!.divisionID, requireContext())
        val bannerService = BannerService(bannerDAO)
        bannerList = bannerService.getBanners4ByDivisionID(viewModel.torneoSeleccionado!!.divisionID)

        val banner : ImageView = binding.banner4
        if (bannerList.isNotEmpty())
        {
            Glide.with(banner.context).load("${bd}/${bannerList[0].link}").into(banner)
        }
    }
}