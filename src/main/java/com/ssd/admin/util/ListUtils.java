package com.ssd.admin.util;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * List 工具类
 */
public class ListUtils {
 
 
    /**
     * 将一个List均分成n个list,主要通过偏移量来实现的
     *
     * @param source 源集合
     * @param limit 最大值
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int limit) {
        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<T>> result = new ArrayList<>();
        int listCount = (source.size() - 1) / limit + 1;
        int remaider = source.size() % listCount; // (先计算出余数)
        int number = source.size() / listCount; // 然后是商
        int offset = 0;// 偏移量
        for (int i = 0; i < listCount; i++) {
            List<T> value;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    public static  <T> List<List<T>> groupList(List<T> source, int limit){
        int listSize = source.size();
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i< listSize; i+=limit) {
            if (i+limit > listSize) {
                limit = listSize - i;
            }
        List newList = source.subList(i, i+limit);
        result.add(new ArrayList<T>(newList));
        }
        return result;
    }

    public static void main(String[] args) {
        List list = new ArrayList();
        for (int i = 0; i < 65; i++) {
            list.add(i);
        }
        List<List> result = groupList(list, 15);
        result.forEach(l -> {
            l.forEach(i ->
                    System.out.print(i + "\t")
            );
            System.out.println();
        });
        System.out.println("====================================================");
        result = averageAssign(list, 20);
        result.forEach(l -> {
            l.forEach(i ->
                    System.out.print(i + "\t")
            );
            System.out.println();
        });
    }
}