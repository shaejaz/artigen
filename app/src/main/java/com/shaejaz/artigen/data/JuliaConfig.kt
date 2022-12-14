package com.shaejaz.artigen.data

data class JuliaConfig(
    override var x: Int, override var y: Int,
    val color1: String,
    val color2: String,
    val color3: String,
    val bgColor: String,
    val min: Int,
    val max: Int,
) : Config
