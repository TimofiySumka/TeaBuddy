package com.example.teabuddy.Teas

import com.example.teabuddy.R

data class TeaModel(
    val image: String="",
    val name: String = "ErrorName",
    val type: String = "ErrorType",
    val description:String = "ErrorDescription",
    val temperature:Int =0,
    val time:Int=0,
    val ingredients: List<String> = emptyList()
) {
    constructor() : this("", "TeaName,","teatype","Description",0,0,emptyList())
}