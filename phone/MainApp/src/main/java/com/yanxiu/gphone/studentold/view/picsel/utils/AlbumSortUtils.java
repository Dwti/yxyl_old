package com.yanxiu.gphone.studentold.view.picsel.utils;

import com.yanxiu.gphone.studentold.view.picsel.bean.ImageBucket;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by lee on 16-4-7.
 */
public class AlbumSortUtils {
    public static List<ImageBucket>  sortListDesc( List<ImageBucket>  list) throws ParseException {
        List<ImageBucket>  retStr=new ArrayList<>();
        Map<Long,ImageBucket> map = new TreeMap<>();
        for(int i=0;i<list.size();i++){
            String dateStr = list.get(i).getDateToken();
            map.put(Long.valueOf(dateStr),list.get(i));
        }
        Collection<ImageBucket> coll=map.values();
        retStr.addAll(coll);
        Collections.reverse(retStr);
        return retStr;
    }
}
