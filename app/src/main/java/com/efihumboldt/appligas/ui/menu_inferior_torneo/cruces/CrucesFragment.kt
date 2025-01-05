package com.efihumboldt.appligas.ui.menu_inferior_torneo.cruces


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.FragmentCrucesBinding


class CrucesFragment : Fragment() {

    private var _binding: FragmentCrucesBinding? = null
    private lateinit var viewModel: DetalleTorneoViewModel
    private lateinit var webView : WebView
    private val bd = SharedDataHolder.bd

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCrucesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val webView : WebView = binding.webView

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(requireContext())

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        webView.loadUrl("$bd/xml/pantalla_cruces.html")
        WebView.setWebContentsDebuggingEnabled(true)
        return root
    }

    private fun cargarXmlDesdeServidor(view: View) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}