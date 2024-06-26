package ru.alexp0111.onigoing.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCiceroneHolder @Inject constructor() {

    private val ciceroneByTabTag = HashMap<String, Cicerone<Router>>()

    fun getCicerone(tabTag: String): Cicerone<Router> = ciceroneByTabTag.getOrPut(tabTag) {
        Cicerone.create()
    }
}