package ru.alexp0111.onigoing.ui.profile.root

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.alexp0111.onigoing.di.modules.ProfileCicerone
import ru.alexp0111.onigoing.navigation.routers.ILocalRouter
import ru.alexp0111.onigoing.ui.base.RootViewModel
import javax.inject.Inject

class ProfileRootViewModel @Inject constructor(
    @ProfileCicerone override val cicerone: Cicerone<Router>,
    @ProfileCicerone override val localRouter: ILocalRouter
) : RootViewModel(cicerone, localRouter)