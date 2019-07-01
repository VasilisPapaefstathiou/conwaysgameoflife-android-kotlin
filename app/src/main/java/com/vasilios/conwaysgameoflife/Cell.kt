package com.vasilios.conwaysgameoflife

import java.io.Serializable

data class Cell(val x: Float, val y: Float, var isAlive: Boolean) : Serializable
