package org.meijer.jelly.jellyFarmService.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Component
@Slf4j
public class GlobalExceptionHandler {
    private static final String EXCEPTION_OCCURED = "Exception occured: ";

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(MethodArgumentNotValidException ex) {
        log.error(EXCEPTION_OCCURED, ex);
        return "Value validation failed for request parameter: " + ex.getParameter().getParameterName();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(MissingServletRequestParameterException ex) {
        log.error(EXCEPTION_OCCURED, ex);
        return "Request parameter was missing: " + ex.getParameterName();

    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(MethodArgumentTypeMismatchException ex) {
        log.error(EXCEPTION_OCCURED, ex);
        return "Type validation failed for request parameter: " + ex.getParameter().getParameterName();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(ConstraintViolationException ex) {
        log.error(EXCEPTION_OCCURED, ex);
        return ex.getMessage();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(HttpMessageNotReadableException ex) {
        log.error(EXCEPTION_OCCURED, ex);
        return ex.getMessage();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handle(NotEnoughRoomInCageException ex) {
        log.error(EXCEPTION_OCCURED, ex);
        return ex.getMessage();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handle(NewCageCannotBeOldCageException ex) {
        log.error(EXCEPTION_OCCURED, ex);
        return ex.getMessage();
    }


    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle(JellyNotFoundException ex) {
        log.error(EXCEPTION_OCCURED, ex);
        return "The requested Jelly couldn't be found or is not for sale.";
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle(CageNotFoundException ex) {
        log.error(EXCEPTION_OCCURED, ex);
        return "The requested Cage couldn't be found.";
    }


    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(Exception ex) {
        log.error(EXCEPTION_OCCURED, ex);
        return "Oops, something went wrong: " + ex.getMessage();
    }

}
