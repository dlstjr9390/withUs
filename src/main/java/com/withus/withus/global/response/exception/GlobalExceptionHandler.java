package com.withus.withus.global.response.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BisException.class)
    public ResponseEntity<ExceptionResponseDto> bisExceptionHandler(BisException e) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(e.getErrorCode());

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e
    ) {
        BindingResult bindingResult = e.getBindingResult();
        String msg = bindingResult.getFieldErrors().get(0).getDefaultMessage();

        return ResponseEntity.status(BAD_REQUEST).body(
                new ExceptionResponseDto(BAD_REQUEST, msg)
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionResponseDto> handleMaxUploadSizeExceeded() {

        return ResponseEntity
            .status(ErrorCode.OVER_FILE_SIZE.getStatus())
            .body(new ExceptionResponseDto(ErrorCode.OVER_FILE_SIZE));
    }

}