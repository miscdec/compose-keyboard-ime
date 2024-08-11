package com.example.composeime.keyboard.common.model

/**
 * Am 2021-01-05 erstellt.
 */


data class Keyboard(val rows: List<KeyRow>) : List<KeyRow> by rows