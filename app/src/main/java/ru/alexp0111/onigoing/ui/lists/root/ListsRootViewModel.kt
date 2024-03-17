package ru.alexp0111.onigoing.ui.lists.root

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.alexp0111.onigoing.di.modules.ListsCicerone
import ru.alexp0111.onigoing.navigation.routers.ILocalRouter
import ru.alexp0111.onigoing.ui.base.RootViewModel
import javax.inject.Inject

class ListsRootViewModel @Inject constructor(
    @ListsCicerone override val cicerone: Cicerone<Router>,
    @ListsCicerone override val localRouter: ILocalRouter
) : RootViewModel(cicerone, localRouter)