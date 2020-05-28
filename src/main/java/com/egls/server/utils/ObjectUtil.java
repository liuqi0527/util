package com.egls.server.utils;

import com.egls.server.utils.array.ArrayUtil;

/**
 * 提供一些对象的方法
 *
 * @author mayer - [Created on 2018-08-09 22:33]
 */
public final class ObjectUtil {

    /**
     * 有任何一个是null,返回true
     */
    public static boolean isAnyNull(final Object... objects) {
        if (objects != null) {
            for (Object e : objects) {
                if (e == null) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * 全都不是null,返回true
     */
    public static boolean isNoneNull(final Object... objects) {
        if (objects != null) {
            for (Object e : objects) {
                if (e == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 全部都是null,返回true
     */
    public static boolean isAllNull(final Object... objects) {
        return ArrayUtil.isAllNull(objects);
    }

}
