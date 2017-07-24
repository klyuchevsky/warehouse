package warehouse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import warehouse.dto.ErrorResponseDTO;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    private ErrorResponseDTO handleError(HttpServletRequest req, Exception e) {
        final String method = req.getMethod();
        final String uri = req.getRequestURI();
        logger.error("Exception while processing request {} {}: {}", method, uri, e.getMessage());
        return new ErrorResponseDTO(method, uri, e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponseDTO illegalStateHandler(HttpServletRequest req, IllegalStateException e) {
        return handleError(req, e);
    }

}
