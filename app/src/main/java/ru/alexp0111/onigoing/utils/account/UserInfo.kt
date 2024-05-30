package ru.alexp0111.onigoing.utils.account

import android.net.Uri

data class UserInfo(
    var id: Int = 0,
    var isLoggedIn: Boolean = false,
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var image: String = Uri.EMPTY.toString(),
)