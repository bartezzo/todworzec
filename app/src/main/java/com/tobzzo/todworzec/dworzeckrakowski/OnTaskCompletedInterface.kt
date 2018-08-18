package com.tobzzo.todworzec.dworzeckrakowski

interface OnTaskCompletedInterface {
    fun onTaskPreparing()

    fun onTaskCompleted(outVersion: Boolean)

    fun hideKeyboard()
}
