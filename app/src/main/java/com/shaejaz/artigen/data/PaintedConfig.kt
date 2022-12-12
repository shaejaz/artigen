package com.shaejaz.artigen.data

data class PaintedConfig(
    override var x: Int, override var y: Int,
    val primaryColor: String,
    val secondaryColor: String,
) : Config
