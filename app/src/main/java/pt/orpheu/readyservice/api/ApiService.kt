package pt.orpheu.readyservice.api

import pt.orpheu.readyservice.model.Item
import pt.orpheu.readyservice.model.Menu
import pt.orpheu.readyservice.model.MenuDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("menus")
    suspend fun getMenus() : List<Menu>

    @GET("menus/{id}/details")
    suspend fun getMenuDetails(@Path("id") id: Long) : MenuDetails

    @GET("items/{id}")
    suspend fun getItemDetails(@Path("id") id: Long) : Item
}