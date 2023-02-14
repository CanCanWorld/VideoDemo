package com.zrq.videodemo

import androidx.lifecycle.ViewModel
import com.zrq.videodemo.bean.Content
import com.zrq.videodemo.bean.ContentData
import com.zrq.videodemo.db.MyDatabase
import com.zrq.videodemo.db.bean.DownloadItem
import com.zrq.videodemo.utils.Constants.PAGE_CONTENT
import java.util.*

class MainModel : ViewModel() {
    var db: MyDatabase? = null
    var keywords = LinkedList<String>()
    var videoId = ""
    var contentData: ContentData? = null
    var onBackPress: () -> Boolean = { true }
    var onRefreshClickListener: () -> Unit = {}
    var nowPage = "SEARCH"
    var setSearchHintText: (String) -> Unit = {}
    var setSearchText: (String) -> Unit = {}
    var clearBottomFocus: () -> Unit = {}
    val localVideo = mutableListOf<DownloadItem>()
    val whereComeToPlayer = PAGE_CONTENT

    init {
    }


    private companion object {
        const val TAG = "MainModel"
    }
}