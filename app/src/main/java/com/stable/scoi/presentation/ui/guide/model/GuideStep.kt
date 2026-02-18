package com.stable.scoi.presentation.ui.guide.model

data class GuideStep(
    val stepNum:String,
    val title:String,
    val description:String,
    val imageRes:Int,
    val noticeText:String?=null,
    val ipAddress: String?=null,
    val exchangeType:String
)
