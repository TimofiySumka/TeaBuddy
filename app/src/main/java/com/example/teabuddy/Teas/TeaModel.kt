package com.example.teabuddy.Teas

import com.example.teabuddy.R

data class TeaModel(
    val image: Int,
    val name: String = "ErrorName",
    val type: String = "ErrorType",
    val description:String = "ErrorDescription",
    val temperature:Int =0,
    val time:Int=0
) {
    constructor() : this(R.drawable.tealeaf, "TeaName,","teatype","Description",0,0)
}