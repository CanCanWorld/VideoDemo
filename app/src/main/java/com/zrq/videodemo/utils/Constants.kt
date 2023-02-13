package com.zrq.videodemo.utils

object Constants {
    const val BASE_URL = "https://api.pingcc.cn"

    const val SEARCH = "/video/search/title"

    const val CONTENT = "/videoChapter/search"

    const val GET_TEXT = "https://api.uomg.com/api/rand.qinghua?format=text"

    private const val KEY = "ff239574c8553cb91b015bad49c2a115"

    const val GET_TEXT_1 = "https://apis.tianapi.com/zaoan/index?key=$KEY"

    const val GET_TEXT_2 = "https://apis.tianapi.com/dialogue/index?key=$KEY"

    const val GET_TEXT_3 = "https://apis.tianapi.com/wanan/index?key=$KEY"

    const val PAGE_COUNT = 20

    const val PAGE_SEARCH = "SEARCH"
    const val PAGE_RESULT = "RESULT"
    const val PAGE_CONTENT = "CONTENT"
    const val PAGE_PLAYER = "PLAYER"
    const val PAGE_DOWN = "DOWN"

    const val DOWN_NON = 0
    const val DOWN_ERROR = -1
    const val DOWN_ING = 1
    const val DOWN_FINISH = 2
    const val DOWN_STOP = 3
}