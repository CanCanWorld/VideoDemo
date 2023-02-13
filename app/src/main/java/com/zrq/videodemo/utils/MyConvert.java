package com.zrq.videodemo.utils;

import android.util.Log;

import com.arialyy.aria.core.processor.IBandWidthUrlConverter;
import com.arialyy.aria.core.processor.IVodTsUrlConverter;

import java.util.ArrayList;
import java.util.List;

public class MyConvert {

    public static class MyBandWidthDefConverter implements IBandWidthUrlConverter {

        @Override
        public String convert(String m3u8Url, String bandWidthUrl) {
            int index = m3u8Url.lastIndexOf("/");
            return m3u8Url.substring(0, index + 1) + bandWidthUrl;
        }
    }

    public static class TSConvert implements IVodTsUrlConverter {

        private static final String TAG = "TSConvert";

        @Override
        public List<String> convert(String m3u8Url, List<String> tsUrls) {
            Log.d(TAG, "convert: " + m3u8Url);
            int index = m3u8Url.lastIndexOf("/");
            List<String> convertedTsUrl = new ArrayList<>();

            String parentUrl = m3u8Url.substring(0, index + 1);
            for (String temp : tsUrls) {
                if (temp.contains("http")) {
                    convertedTsUrl.add(temp);
                    Log.d(TAG, "temp: " + temp);
                } else {
                    convertedTsUrl.add(parentUrl + temp);
                    Log.d(TAG, "parentUrl + temp: " + parentUrl + temp);
                }
            }
            return convertedTsUrl;
        }
    }
}
