package pt.orpheu.readyservice.di.module

import dagger.Binds
import dagger.Module
import pt.orpheu.readyservice.repository.OrdersRepository
import pt.orpheu.readyservice.repository.OrdersRepositoryImpl
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOrdersRepository(OrdersRepositoryImpl: OrdersRepositoryImpl) : OrdersRepository

}