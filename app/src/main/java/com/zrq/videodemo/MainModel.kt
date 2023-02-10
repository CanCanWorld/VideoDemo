package com.zrq.videodemo

import androidx.lifecycle.ViewModel
import com.zrq.videodemo.bean.Content
import com.zrq.videodemo.db.MyDatabase
import java.util.*

class MainModel : ViewModel() {
    var db: MyDatabase? = null
    var keywords = LinkedList<String>()
    var videoId = ""
    var content: Content? = null
    var onBackPress: () -> Boolean = { true }
    var onRefreshClickListener: () -> Unit = {}
    var nowPage = "SEARCH"
    var setSearchHintText: (String) -> Unit = {}
    var setSearchText: (String) -> Unit = {}
}