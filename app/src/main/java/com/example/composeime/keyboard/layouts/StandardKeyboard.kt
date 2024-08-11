package com.example.composeime.keyboard.layouts

import com.example.composeime.R
import com.example.composeime.keyboard.common.dsl.key
import com.example.composeime.keyboard.common.dsl.keyboard
import com.example.composeime.keyboard.common.dsl.keys
import com.example.composeime.keyboard.common.dsl.row
import com.example.composeime.keyboard.common.model.Key


val standardKeyboard = keyboard {
    row("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    row("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P")
    row("A", "S", "D", "F", "G", "H", "J", "K", "L", width = .9f)
    row {
        key(
            iconRes = R.drawable.ic_keyboard_arrow,
            action = Key.Action.Perform.Shift,
            weight = 2f,
            animation = Key.Animation.PRESS,
            background = Key.Background.TRANSPARENT
        )
        keys("Z", "X", "C", "V", "B", "N", "M")
        key(
            iconRes = R.drawable.ic_keyboard_backspace,
            action = Key.Action.Perform.Delete,
            weight = 2f,
            animation = Key.Animation.PRESS,
            background = Key.Background.TRANSPARENT
        )
    }
    row {
        key(
            "!#1",
            action = Key.Action.Perform.ChangeMode,
            weight = 2f,
            animation = Key.Animation.PRESS,
            background = Key.Background.TRANSPARENT
        )
        key(",", input = ",", weight = 1f, background = Key.Background.TRANSPARENT)
        key(
            iconRes = R.drawable.ic_keyboard_globe,
            action = Key.Action.Perform.Switch,
            weight = 1f,
            animation = Key.Animation.PRESS,
            background = Key.Background.TRANSPARENT
        )
        key(" ", action = Key.Action.Perform.Space, weight = 4f, animation = Key.Animation.PRESS)
        key(".", input = ".", weight = 1f, background = Key.Background.TRANSPARENT)
        key(
            iconRes = R.drawable.ic_keyboard_confirm,
            action = Key.Action.Perform.Enter,
            weight = 2f,
            animation = Key.Animation.PRESS,
            background = Key.Background.TRANSPARENT
        )
    }
}