package ru.alexp0111.onigoing.di.modules

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.alexp0111.onigoing.navigation.LocalCiceroneHolder
import ru.alexp0111.onigoing.navigation.NavigationTabTags
import ru.alexp0111.onigoing.navigation.routers.ILocalRouter
import ru.alexp0111.onigoing.navigation.routers.ListsRouter
import ru.alexp0111.onigoing.navigation.routers.ProfileRouter
import ru.alexp0111.onigoing.navigation.routers.SearchRouter
import ru.alexp0111.onigoing.utils.account.ProfileManager
import javax.inject.Singleton

@Module
class NavigationModule {
    @Singleton
    @Provides
    @SearchCicerone
    fun provideSearchCicerone(ciceroneHolder: LocalCiceroneHolder): Cicerone<Router> {
        return ciceroneHolder.getCicerone(NavigationTabTags.TAG_SEARCH)
    }

    @Singleton
    @Provides
    @ListsCicerone
    fun provideListsCicerone(ciceroneHolder: LocalCiceroneHolder): Cicerone<Router> {
        return ciceroneHolder.getCicerone(NavigationTabTags.TAG_LISTS)
    }

    @Singleton
    @Provides
    @ProfileCicerone
    fun provideProfileCicerone(ciceroneHolder: LocalCiceroneHolder): Cicerone<Router> {
        return ciceroneHolder.getCicerone(NavigationTabTags.TAG_PROFILE)
    }

    @Singleton
    @Provides
    fun provideSearchRouter(@SearchCicerone cicerone: Cicerone<Router>): SearchRouter =
        SearchRouter(cicerone.router)

    @Singleton
    @Provides
    fun provideListsRouter(@ListsCicerone cicerone: Cicerone<Router>): ListsRouter =
        ListsRouter(cicerone.router)

    @Singleton
    @Provides
    fun provideProfileRouter(@ProfileCicerone cicerone: Cicerone<Router>, profileManager: ProfileManager): ProfileRouter =
        ProfileRouter(cicerone.router, profileManager)

    @Singleton
    @Provides
    @SearchCicerone
    fun provideSearchLocalRouter(router: SearchRouter): ILocalRouter = router

    @Singleton
    @Provides
    @ListsCicerone
    fun provideListsLocalRouter(router: ListsRouter): ILocalRouter = router

    @Singleton
    @Provides
    @ProfileCicerone
    fun provideProfileLocalRouter(router: ProfileRouter): ILocalRouter = router
}