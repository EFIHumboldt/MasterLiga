package com.efihumboldt.appligas.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.efihumboldt.appligas.ui.activities.Ligas.LigaActivity
import com.efihumboldt.appligas.databinding.ActivityAnimMainActivityBinding

class AnimMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimMainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        supportActionBar?.hide()


        binding = ActivityAnimMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text_aux : TextView = binding.textAux



        text_aux.setOnClickListener {

            val intent = Intent(this, LigaActivity::class.java)
            startActivity(intent)
        }
    }


}