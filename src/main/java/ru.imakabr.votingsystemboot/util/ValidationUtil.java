package ru.imakabr.votingsystemboot.util;


import org.slf4j.Logger;

import ru.imakabr.votingsystemboot.model.AbstractBaseEntity;
import ru.imakabr.votingsystemboot.model.Item;
import ru.imakabr.votingsystemboot.util.exception.ErrorType;
import ru.imakabr.votingsystemboot.util.exception.NotFoundException;
import ru.imakabr.votingsystemboot.util.exception.TimeVoteLimitException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public final static LocalTime timeRestriction = LocalTime.of(11, 00); // 11 00
    private final static DateTimeFormatter timePattern = DateTimeFormatter.ofPattern("hh:mm:ss a");

    public static void checkDateTime(LocalDate date) {
        LocalDate today = LocalDate.now();
        if (!today.isEqual(date)) {
            throw new TimeVoteLimitException("You voted on " + date + ". You can not change it today " + today);
        }
        checkTime();
    }

    public static void checkTime() {
        LocalTime now = LocalTime.now();
        if (timeRestriction.isBefore(now)) {
            throw new TimeVoteLimitException("Time is " + now.format(timePattern) + ". You can not vote after " + timeRestriction.format(timePattern));
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    public static void assureItemIdConsistent(Item item, int id) {
        if (item.isNew()) {
            item.setId(id);
        } else if (item.getId() != id) {
            throw new IllegalArgumentException(item + " must be with id=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }
}
