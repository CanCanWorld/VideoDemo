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

    const val PAGE_COUNT = 30

    const val PAGE_SEARCH = "SEARCH"
    const val PAGE_RESULT = "RESULT"
    const val PAGE_CONTENT = "CONTENT"
    const val PAGE_PLAYER = "PLAYER"
    const val PAGE_DOWNLOADED = "DOWNLOADED"
    const val PAGE_DOWNLOADING = "DOWNLOADING"
    const val PAGE_SETTING = "SETTING"

    const val DOWN_NON = 0
    const val DOWN_FAIL = -1
    const val DOWN_RUN = 1
    const val DOWN_COMPLETE = 2
    const val DOWN_STOP = 3
    const val DOWN_PRE = 4
    const val DOWN_WAIT = 5

    const val THEME_TYPE = "theme_type"
    const val SPEED = "speed"
    const val THEME_RED = "red"
    const val THEME_PINK = "pink"
    const val THEME_PURPLE = "purple"
    const val THEME_BLUE = "blue"
    const val THEME_GREEN = "green"
    const val THEME_YELLOW = "yellow"
}