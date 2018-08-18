package com.tobzzo.todworzec.dworzeckrakowski.tools

import android.os.AsyncTask
import android.text.Editable
import android.text.TextWatcher

abstract class DelayedTextWatcher(private val delayTime: Long) : TextWatcher {
    private var lastWaitTask: WaitTask? = null

    override fun afterTextChanged(s: Editable) {
        synchronized(this) {
            if (lastWaitTask != null) {
                lastWaitTask!!.cancel(true)
            }
            lastWaitTask = WaitTask()
            lastWaitTask!!.execute(s)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                   after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit

    abstract fun afterTextChangedDelayed(s: Editable)

    private inner class WaitTask : AsyncTask<Editable, Void, Editable>() {

        override fun doInBackground(vararg params: Editable): Editable {
            try {
                Thread.sleep(delayTime)
            } catch (e: InterruptedException) {
            }

            return params[0]
        }

        override fun onPostExecute(result: Editable) {
            super.onPostExecute(result)
            afterTextChangedDelayed(result)
        }
    }

}