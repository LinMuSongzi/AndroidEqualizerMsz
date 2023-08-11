package com.bullhead.equalizer

import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.io.Serializable

class SimpleViewModel(var savedStateHandle: SavedStateHandle) : ViewModel() {


    var musicUri: Uri?
        get() {
            return savedStateHandle.get("musicUri")
        }
        set(value) {
            savedStateHandle["musicUri"] = value
        }

    var position: Int
        get() {
            return savedStateHandle["position"] ?: -1
        }
        set(value) {
            savedStateHandle["position"] = position
        }

    companion object {
        const val REMARK_LIST_KEY = "getLifeRemarkList"
    }

    fun getLifeRemarkList(): List<String> = savedStateHandle[REMARK_LIST_KEY] ?: let {
        SavedList<String>(this).apply {
            savedStateHandle[REMARK_LIST_KEY] = this
        }
    }

    fun observerRemarkList(lifecycleOwner: LifecycleOwner, observer: Observer<List<CharSequence>>) {
        savedStateHandle.getLiveData<List<CharSequence>>(REMARK_LIST_KEY).observe(lifecycleOwner, observer)
    }

    fun observerMusicUri(lifecycleOwner: LifecycleOwner, observer: Observer<Uri?>) {
        savedStateHandle.getLiveData<Uri?>("musicUri").observe(lifecycleOwner, observer)
    }

    fun observerChoosePosition(lifecycleOwner: LifecycleOwner, observer: Observer<Int>) {
        savedStateHandle.getLiveData<Int>("position").observe(lifecycleOwner, observer)
    }


    inner class SavedList<T : Serializable>(var vm: SimpleViewModel) : ArrayList<T>() {

        override fun add(element: T): Boolean {
            return super.add(element).apply {
                savedStateHandle[REMARK_LIST_KEY] = this@SavedList
            }
        }

        override fun clear() {
            super.clear()
            savedStateHandle[REMARK_LIST_KEY] = this@SavedList
        }

        override fun remove(element: T): Boolean {
            return super.remove(element).apply {
                savedStateHandle[REMARK_LIST_KEY] = this@SavedList
            }
        }

    }

}