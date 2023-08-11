package com.bullhead.androidequalizer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import java.io.Serializable

class SimpleViewModel(var savedStateHandle: SavedStateHandle) : ViewModel() {

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