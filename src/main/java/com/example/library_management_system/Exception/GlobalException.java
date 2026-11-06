package com.example.library_management_system.Exception;

import com.example.library_management_system.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(GenreException.class)
    public ResponseEntity<ApiResponse> handleGenreException(GenreException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(ex.getMessage(), false)
        );
    }
}
