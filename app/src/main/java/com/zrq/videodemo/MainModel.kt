package com.zrq.videodemo

import androidx.lifecycle.ViewModel
import com.zrq.videodemo.bean.Content
import com.zrq.videodemo.bean.Result
import java.util.*
import kotlin.collections.HashMap

class MainModel : ViewModel() {
    var keywords = LinkedList<String>()
    var videoId = ""
    var content: Content? = null
    var onBackPress: () -> Boolean = { true }
    var onRefreshClickListener: () -> Unit = {}
    var nowPage = "SEARCH"
    var setSearchHintText: (String) -> Unit = {}
    var setSearchText: (String) -> Unit = {}
}