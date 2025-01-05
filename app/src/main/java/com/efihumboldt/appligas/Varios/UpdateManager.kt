package com.efihumboldt.appligas.Varios

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class UpdateManager() {
    fun checkAndForceUpdate(context: Context) {

        val appUpdateManager = AppUpdateManagerFactory.create(context)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                showUpdateDialog(context)
            }
        }
    }


    private fun showUpdateDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Actualización Obligatoria")
            .setMessage("Una nueva versión de la aplicación está disponible. Debes actualizar para seguir utilizando la aplicación.")
            .setPositiveButton("Actualizar") { dialog, which ->
                // Redirigir al usuario a la página de la aplicación en la Play Store
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.efihumboldt.appligas"))
                context.startActivity(intent)
            }
            .setCancelable(false)
            .show()
    }
}