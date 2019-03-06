package com.billetsdoux.webfluxcommon.advice;

import com.billetsdoux.webfluxcommon.exception.StudentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;


/**
 * 验证失败，则返回失败信息及400状态码
 */
@ControllerAdvice // 表示当前类为一个通知（切面）,其连接点为处理方法
public class ParamterValidateAdvice {

    @ExceptionHandler
    public ResponseEntity<String> validateHandle(StudentException ex) {

        String msg = ex.getMessage() + "【" + ex.getErrValue() + ":" + ex.getErrField() + "】";
        return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
    }

    // 将所有的异常信息转换为一个str
    private String exToStr(WebExchangeBindException ex) {
        return ex.getFieldErrors()
                .stream()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .reduce("", (s1, s2) -> s1 + "\n" + s2);
    }
}
