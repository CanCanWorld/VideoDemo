package com.zrq.videodemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zrq.videodemo.bean.Content
import com.zrq.videodemo.bean.DataItem

class MainModel : ViewModel() {
    val searchResult = MutableLiveData<MutableList<DataItem>>()
    var videoId = ""
    var content: Content? = null
    var onBackPress: () -> Boolean = { true }
}