package uk.co.netbans.discordbot;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtil {
    public static <T> List<T> of(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static <T> List<T> ofI(T... elements) {
        return ImmutableList.copyOf(ListUtil.of(elements));
    }
}
