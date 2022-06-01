package com.grgbanking.core.utils;
import java.util.LinkedList;
/**
 * @ClassName SetList
 * @Description 有序且不重复的list集合
 * @Author Jesson
 * @Date 2018/9/6 15:13
 * @Version 1.0
 **/
public class SetList<T> extends LinkedList<T> {
    private static final long serialVersionUID = 1434324234L;

    @Override
    public boolean add(T object) {
        if (size() == 0) {
            return super.add(object);
        } else {
            int count = 0;
            for (T t : this) {
                if (t.equals(object)) {
                    count++;
                    break;
                }
            }
            if (count == 0) {
                return super.add(object);
            } else {
                return false;
            }
        }
    }
}

