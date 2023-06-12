package com.weinfuse.beassessment.controller.bowling;

import com.weinfuse.beassessment.bos.bowling.CalculateScoreRequest;
import com.weinfuse.beassessment.bos.bowling.CalculateScoreResponse;
import com.weinfuse.beassessment.service.bowling.BowlingScoreCalculatorService;
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

    // todo add validation to make sure array is good
    @RequestMapping(path = "/calculateScores", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<CalculateScoreResponse> calculateBowlingScores(@RequestBody CalculateScoreRequest calculateScoreRequest) {
        CalculateScoreResponse calculateScoreResponse = bowlingScoreCalculatorService.calculateBowlingScores(calculateScoreRequest);
        return new ResponseEntity<>(calculateScoreResponse, HttpStatus.OK);
    }
}
