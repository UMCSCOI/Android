package com.stable.scoi.presentation.ui.transfer.dialog

import android.widget.EditText

interface SetBookmark {
        fun inputString(name: String, address: String)
        fun setExchangeUPBIT()
        fun setExchangeBITHUMB()
        fun setExchangeBINANCE()
        fun removeFocus(editText: EditText)
}