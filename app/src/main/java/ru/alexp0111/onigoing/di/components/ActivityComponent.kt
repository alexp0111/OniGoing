package ru.alexp0111.onigoing.di.components

import android.app.Activity
import ru.alexp0111.onigoing.ui.MainActivity

interface ActivityComponent {

    fun inject(activity: MainActivity)

    companion object {
        fun from(activity: Activity) = AppComponent.from(activity.applicationContext)
    }
}