package project.hackmol.hackmolinstafix

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (!isInPreviewMode()) {
            FirebaseApp.initializeApp(this)
            val firebaseAppCheck = FirebaseAppCheck.getInstance()
            firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )
        }
    }

    fun isInPreviewMode(): Boolean {
        return android.os.Build.VERSION.SDK_INT == 0
    }
}