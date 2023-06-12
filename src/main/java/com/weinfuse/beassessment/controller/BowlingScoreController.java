package com.weinfuse.beassessment.controller;

import com.weinfuse.beassessment.bos.CalculateScoreRequest;
import com.weinfuse.beassessment.bos.CalculateScoreResponse;
import com.weinfuse.beassessment.service.BowlingScoreCalculatorService;
import com.weinfuse.beassessment.validation.BowlingScoreRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping(value = "/bowling")
public class BowlingScoreController {

    @Autowired
    private BowlingScoreCalculatorService bowlingScoreCalculatorService;


    @RequestMapping(path = "/calculateScores", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<CalculateScoreResponse> calculateBowlingScores(@RequestBody CalculateScoreRequest calculateScoreRequest) {
        CalculateScoreResponse calculateScoreResponse = BowlingScoreRequestValidator.staticValidator.validateBowlingScoreCalculationRequest(calculateScoreRequest);

        if(calculateScoreResponse != null) {
            return new ResponseEntity<>(calculateScoreResponse, HttpStatus.BAD_REQUEST);
        }

        // theoretically, the validation should have prevented any issues that would come up here, so we'll just return what it returns
        // in a more robust system you'd have error handling in the service class so it won't always return what you expect, but it should here
        calculateScoreResponse = bowlingScoreCalculatorService.calculateBowlingScores(calculateScoreRequest);
        return new ResponseEntity<>(calculateScoreResponse, HttpStatus.OK);
    }
}
