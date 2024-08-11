package com.example.composeime.keyboard.common.dsl

import com.example.composeime.keyboard.common.model.Key
import com.example.composeime.keyboard.common.model.KeyRow

/**
 * Am 2021-01-05 erstellt.
 */

const val MAX_ROW_WIDTH = 1.0f

class KeyRowBuilder(private val width: Float) :
    Builder<KeyRow, KeyRowBuilder.Key>() {


    data class Key(
        val label: com.example.composeime.keyboard.common.model.Key.Label,
        val action: com.example.composeime.keyboard.common.model.Key.Action,
        val absoluteWeight: Float,
        val animation: com.example.composeime.keyboard.common.model.Key.Animation = com.example.composeime.keyboard.common.model.Key.Animation.POPUP,
        val background: com.example.composeime.keyboard.common.model.Key.Background = com.example.composeime.keyboard.common.model.Key.Background.FILLED
    )

    init {
        if (width > MAX_ROW_WIDTH) throw IllegalArgumentException("Width is greater than $MAX_ROW_WIDTH (actual: $width)")
    }

    override fun create(elements: List<Key>): KeyRow {
        val treatAsRelativeWeight = elements.all { it.absoluteWeight < 1f }
        val absoluteWeightSum = elements.sumOf { it.absoluteWeight.toDouble() }
        return KeyRow(
            keys = elements.map {
                com.example.composeime.keyboard.common.model.Key(
                    it.label,
                    it.action,
                    roundToNDecimalPlaces(
                        if (treatAsRelativeWeight) it.absoluteWeight / elements.size.toDouble() else it.absoluteWeight / absoluteWeightSum,
                        4
                    ) * width,
                    it.animation,
                    background = it.background
                )
            },
            width = width
        )
    }
}