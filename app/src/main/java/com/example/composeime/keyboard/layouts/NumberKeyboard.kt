package com.example.composeime.keyboard.layouts

import com.example.composeime.R
import com.example.composeime.keyboard.common.dsl.key
import com.example.composeime.keyboard.common.dsl.keyboard
import com.example.composeime.keyboard.common.dsl.keys
import com.example.composeime.keyboard.common.dsl.row
import com.example.composeime.keyboard.common.model.Key

val numberKeyboard = keyboard {
    row {
        keys("1", "2", "3")
        key(
            iconRes = R.drawable.ic_keyboard_backspace,
            action = Key.Action.Perform.Delete,
            weight = 1f,
            animation = Key.Animation.PRESS,
            background = Key.Background.TRANSPARENT
        )
    }
    row {
        keys("4", "5", "6")
        key(
            iconRes = R.drawable.ic_keyboard_arrow,
            action = Key.Action.Perform.Shift,
            weight = 1f,
            animation = Key.Animation.PRESS,
            background = Key.Background.TRANSPARENT
        )
    }
    row {
        keys("7", "8", "9")
        key(
            " ",
            action = Key.Action.Perform.Space,
            weight = 1f,
            animation = Key.Animation.PRESS
        )
    }
    row {
        key(
            "!#1",
            action = Key.Action.Perform.ChangeMode,
            weight = 1f,
            animation = Key.Animation.PRESS,
            background = Key.Background.TRANSPARENT
        )
        key("0")
        key(
            iconRes = R.drawable.ic_keyboard_globe,
            action = Key.Action.Perform.Switch,
            weight = 1f,
            animation = Key.Animation.PRESS,
            background = Key.Background.TRANSPARENT
        )
        key(
            iconRes = R.drawable.ic_keyboard_confirm,
            action = Key.Action.Perform.Enter,
            weight = 1f,
            animation = Key.Animation.PRESS,
            background = Key.Background.TRANSPARENT
        )

    }
}