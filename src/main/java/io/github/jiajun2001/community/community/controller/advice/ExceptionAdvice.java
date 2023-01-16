package io.github.jiajun2001.community.community.controller.advice;

import io.github.jiajun2001.community.community.util.CommunityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;

// Handle exceptions for all classes with "Controller" annotation
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class}) // Handle all exception classes
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Logging errors
        logger.error("500: " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }

        // Check if it is an asynchronous request or not
        String xRequestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            // If it is an asynchronous request
            // Return a plain text for AJAX to convert it to JSON format
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "Server Error!"));
        } else {
            // If it is a normal request
            // Redirect the user to error pages
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}