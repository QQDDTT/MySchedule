package com.local.MySchedule.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class MyExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException ex, Model model) {
        model.addAttribute("Error", "页面未找到");
        LOGGER.error("404错误: {}", ex.getMessage());
        return "/error"; // 返回404错误页面
    }

    @ExceptionHandler(Exception.class)
    public String handle500(Exception ex, Model model) {
        model.addAttribute("Error", "服务器内部错误");
        LOGGER.error("500错误: {}", ex.getMessage());
        return "/error"; // 返回500错误页面
    }
}
