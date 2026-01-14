package com.stable.scoi.presentation.base

data class Reciever(
    var recieverName: String = "",
    var recieverType: RecieverType,
    var adress: String = ""
)

sealed class RecieverType {
    object Individual: RecieverType()
    object Corporation: RecieverType()
    object Null: RecieverType()
}

