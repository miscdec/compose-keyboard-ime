package com.example.composeime.ime.platform.android.operation

import android.content.Context
import com.example.composeime.ime.platform.android.service.IMEService
import com.example.composeime.keyboard.common.dsl.key
import com.example.composeime.keyboard.common.dsl.keyboard
import com.example.composeime.keyboard.common.dsl.keys
import com.example.composeime.keyboard.common.dsl.row
import com.example.composeime.keyboard.common.model.Key
import com.example.composeime.keyboard.common.model.Keyboard
import com.example.composeime.keyboard.layouts.numberKeyboard

fun onKeyPress(ctx: Context, onSwitch: () -> Unit, onChangeMode: () -> Unit): (Key) -> Unit = {
    when (it.action) {
        Key.Action.Perform.Delete -> {
            (ctx as IMEService).currentInputConnection.deleteSurroundingText(1, 0)
        }

        Key.Action.Perform.Enter -> {
            (ctx as IMEService).currentInputConnection.commitText("\n", 1)
        }

        Key.Action.Perform.Space -> {
            (ctx as IMEService).currentInputConnection.commitText(" ", 1)
        }

        is Key.Action.Input -> {
            (ctx as IMEService).currentInputConnection.commitText(
                it.action.chars.concatToString(),
                it.action.size
            )
        }

        Key.Action.Perform.ChangeMode -> onChangeMode()

        Key.Action.Perform.Shift -> {
            (ctx as IMEService).currentInputConnection.setComposingText("", 1)
        }

        Key.Action.Perform.Switch -> onSwitch()
    }
}
