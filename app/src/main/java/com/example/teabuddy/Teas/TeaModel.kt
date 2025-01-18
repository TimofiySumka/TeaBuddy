package com.example.teabuddy.Teas

import com.example.teabuddy.R

data class TeaModel(
    val image: Int,
    val name: String = "",
    val type: String = ""
) {
    constructor() : this(R.drawable.tealeaf, "TeaName,","teatype")
}