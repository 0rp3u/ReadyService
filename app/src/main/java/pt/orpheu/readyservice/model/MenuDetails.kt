package pt.orpheu.readyservice.model

data class MenuDetails(
        val id: Long,
        val name: String,
        val imageUrl: String,
        val subMenus: List<SubMenu>

)