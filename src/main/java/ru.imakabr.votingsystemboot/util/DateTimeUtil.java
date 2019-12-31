package ru.imakabr.votingsystemboot.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

}
