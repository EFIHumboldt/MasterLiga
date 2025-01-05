package com.efihumboldt.appligas.Varios

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.ui.activities.Ligas.LigaActivity
import android.app.ActivityManager
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val gifImageView = findViewById<ImageView>(R.id.gifImageView)

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val availableMemory = memoryInfo.availMem

        val threshold = 100 * 1024 * 1024 // 100 MB

        if (availableMemory > threshold) {

            Glide.with(this)
                .asGif()
                .load(R.drawable.gif_50)
                .into(gifImageView)
        } else {
            Glide.with(this)
                .load(R.drawable.banner)
                .into(gifImageView)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LigaActivity::class.java))
            finish()
        }, 2700)
    }
}
