package com.tobzzo.todworzec.dworzeckrakowski

import android.support.v7.app.AppCompatActivity

open class ToMdaBaseActivity : AppCompatActivity() {
    protected open val className: String
        get() {
            return this.localClassName
        }
}