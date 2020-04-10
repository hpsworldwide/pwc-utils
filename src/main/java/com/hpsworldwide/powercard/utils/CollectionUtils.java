package com.hpsworldwide.powercard.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author (c) HPS Solutions
 */
public class CollectionUtils {

    /**
     * when Collections.reverse(list) isn't supported
     */
    public static List reverse(final Collection collection) {
        List result = new ArrayList();
        for (Object element : collection) {
            result.add(0, element);
        }
        return result;
    }
}
