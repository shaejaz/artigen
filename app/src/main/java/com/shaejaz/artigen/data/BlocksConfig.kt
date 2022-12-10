package com.shaejaz.artigen.data

data class BlocksConfig(
    var x: Int,
    var y: Int,
    var color1: String,
    var color2: String,
    var color3: String,
    var bgColor: String,
    var blockSize: Int,
    var lineSize: Int,
    var density: Float,
) : Config