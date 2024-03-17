package ru.alexp0111.onigoing.ui.search.root

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.alexp0111.onigoing.di.modules.SearchCicerone
import ru.alexp0111.onigoing.navigation.routers.ILocalRouter
import ru.alexp0111.onigoing.ui.base.RootViewModel
import javax.inject.Inject

class SearchRootViewModel @Inject constructor(
    @SearchCicerone override val cicerone: Cicerone<Router>,
    @SearchCicerone override val localRouter: ILocalRouter
) : RootViewModel(cicerone, localRouter)