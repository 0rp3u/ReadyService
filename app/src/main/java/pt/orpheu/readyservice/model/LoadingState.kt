package pt.orpheu.readyservice.model


sealed class LoadingState
data class ERROR(val error: String) : LoadingState()
object LOADED : LoadingState()
object LOADING : LoadingState()