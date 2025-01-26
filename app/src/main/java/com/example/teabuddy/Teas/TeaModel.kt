package com.example.teabuddy.Teas

import com.example.teabuddy.R

data class TeaModel(
    val image: String="https://firebasestorage.googleapis.com/v0/b/teabuddy-e6bea.firebasestorage.app/o/TeaImages%2Flovare_strawberry_champagne.jpg?alt=media&token=100b4e02-fac3-40a2-9ac1-7fe1b64bef82",
    val name: String = "ErrorName",
    val type: String = "ErrorType",
    val description:String = "ErrorDescription",
    val temperature:Int =0,
    val time:Int=0,
    val caffeine:Int=0,
    val ingredients: List<String> = emptyList()
) {
    constructor() : this("", "TeaName,","teatype","Description",0,0,0,emptyList())
}