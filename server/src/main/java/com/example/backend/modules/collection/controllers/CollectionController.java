package com.example.backend.modules.collection.controllers;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.auth.dtos.RegisterDTO;
import com.example.backend.modules.collection.services.CollectionService;
import com.example.backend.modules.user.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collections") // http://localhost:8080/api/collections
public class CollectionController {
    private final CollectionService collectionService;

    public CollectionController(
            CollectionService collectionService
    ){
        this.collectionService = collectionService;
    }

    @PostMapping("/{quizId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> addToCollection(
            @PathVariable("quizId") int quizId,
            @AuthenticationPrincipal User user
            ) {
        var result = collectionService.addToCollection(user,quizId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{quizId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> removeFromCollection(
            @PathVariable("quizId") int quizId,
            @AuthenticationPrincipal User user
    ) {
        var result = collectionService.removeFromCollection(user,quizId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
