package ru.imakabr.votingsystemboot.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.imakabr.votingsystemboot.util.ValidationUtil;
import ru.imakabr.votingsystemboot.util.exception.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

import static ru.imakabr.votingsystemboot.util.exception.ErrorType.*;

@RestControllerAdvice
public class ExceptionInfoHandler  {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";
    public static final String EXCEPTION_DUPLICATE_ITEM = "You already have item with this name and date for this restaurant";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT = "You already have restaurant with this name";
    public static final String EXCEPTION_DUPLICATE_VOTE = "You already have vote for this restaurant";

    private static final Map<String, String> CONSTRAINS_MAP = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "restaurants_idx", EXCEPTION_DUPLICATE_RESTAURANT,
            "votes_idx", EXCEPTION_DUPLICATE_VOTE,
            "items_idx", EXCEPTION_DUPLICATE_ITEM);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorInfo wrongRequest(HttpServletRequest req, NoHandlerFoundException e) throws Exception {
        return logAndGetErrorInfo(req, e, false, ErrorType.WRONG_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(TimeVoteLimitException.class)
    public ErrorInfo timeVoteLimitError(HttpServletRequest req, TimeVoteLimitException e) {
        return logAndGetErrorInfo(req, e, true, VOTING_ERROR);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            Optional<Map.Entry<String, String>> entry = CONSTRAINS_MAP.entrySet().stream()
                    .filter(it -> lowerCaseMsg.contains(it.getKey()))
                    .findAny();
            if (entry.isPresent()) {
                return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, entry.get().getValue());
            }
        }
        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ErrorInfo bindValidationError(HttpServletRequest req, Exception e) {
        BindingResult result = e instanceof BindException ?
                ((BindException) e).getBindingResult() : ((MethodArgumentNotValidException) e).getBindingResult();

        String[] details = result.getFieldErrors().stream()
                .map(x -> x.getField() + ": " + x.getDefaultMessage())
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, details);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logException, errorType);
        return new ErrorInfo(req.getRequestURL(), errorType,
                errorType.getErrorCode(),
                details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)});
    }

}