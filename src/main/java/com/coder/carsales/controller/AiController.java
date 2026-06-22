package com.coder.carsales.controller;

import com.coder.carsales.service.AiQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiQueryService aiService;

    public AiController(AiQueryService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> ask(
            @RequestBody String question) {

        return ResponseEntity.ok(
                aiService.process(question)
        );
    }
}