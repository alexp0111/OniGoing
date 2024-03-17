package ru.alexp0111.onigoing

import android.app.Application
import ru.alexp0111.onigoing.di.components.AppComponent
import ru.alexp0111.onigoing.di.components.DaggerAppComponent
import ru.alexp0111.onigoing.di.modules.AppModule

class OniGoingApplication : Application() {

    val appComponent: AppComponent by lazy {
        initComponent()
    }

    private fun initComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}