package com.example.backend.modules.filestorage.exceptions;


import com.cloudinary.api.exceptions.BadRequest;
import com.example.backend.commons.ResponseError;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FileStorageExceptionHandler {
    @ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError badRequestUploadFileExceptionHandler(BadRequest ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,"Upload tệp thất bại");
    }

    @ExceptionHandler(FileIsEmptyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError fileIsEmptyExceptionHandler(FileIsEmptyException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(FileTooLargeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError fileTooLargeExceptionHandler(FileTooLargeException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(NotAllowMimeTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError notAllowMimeTypeExceptionHandler(NotAllowMimeTypeException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError fileSizeLimitExceededExceptionHandler(FileSizeLimitExceededException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,"Dung lượng file vượt quá 3mb.");
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError SizeLimitExceededExceptionHandler(SizeLimitExceededException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,"Dung lượng request vượt quá 10mb");
    }
}
