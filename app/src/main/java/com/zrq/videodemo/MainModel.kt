package com.zrq.videodemo

import androidx.lifecycle.ViewModel
import com.zrq.videodemo.bean.Content

class MainModel : ViewModel() {
    var keyword = ""
    var videoId = ""
    var content: Content? = null
    var onBackPress: () -> Boolean = { true }
    var onRefreshClickListener: () -> Unit = {}
    var nowPage = "SEARCH"
}