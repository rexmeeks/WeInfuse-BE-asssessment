package com.weinfuse.beassessment.controller;

import com.weinfuse.beassessment.bos.CalculateScoreRequest;
import com.weinfuse.beassessment.bos.CalculateScoreResponse;
import com.weinfuse.beassessment.service.BowlingScoreCalculatorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BowlingScoreControllerTests {

    @Autowired
    private BowlingScoreController bowlingScoreController;

    @Mock
    private BowlingScoreCalculatorService bowlingScoreCalculatorService;


    @Test
    public void normalBowlingRequest() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        List<List<String>> frameScores = new ArrayList<>();
        frameScores.add(Arrays.asList("1", "2", "X", "9", "/"));
        calculateScoreRequest.setIndividualFrameScores(frameScores);
        // since I'm not using any external services, I could just use this whole thing as the test, but I'd rather keep it like it should be, with the mocks
        when(bowlingScoreCalculatorService.calculateBowlingScores(any())).thenReturn(new CalculateScoreResponse());
        ResponseEntity<CalculateScoreResponse> calculateScoreResponseResponseEntity = bowlingScoreController.calculateBowlingScores(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponseResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void doubleScoreBowlingRequest() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        List<List<String>> frameScores = new ArrayList<>();
        frameScores.add(Arrays.asList("1", "2", "X", "9", "/"));
        frameScores.add(Arrays.asList("2", "/", "1", "4", "X"));
        calculateScoreRequest.setIndividualFrameScores(frameScores);
        // since I'm not using any external services, I could just use this whole thing as the test, but I'd rather keep it like it should be, with the mocks
        when(bowlingScoreCalculatorService.calculateBowlingScores(any())).thenReturn(new CalculateScoreResponse());
        ResponseEntity<CalculateScoreResponse> calculateScoreResponseResponseEntity = bowlingScoreController.calculateBowlingScores(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponseResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void badBowlingRequest() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        List<List<String>> frameScores = new ArrayList<>();
        frameScores.add(Arrays.asList("1", "2", "X", "L", "-"));
        calculateScoreRequest.setIndividualFrameScores(frameScores);
        // since I'm not using any external services, I could just use this whole thing as the test, but I'd rather keep it like it should be, with the mocks
        when(bowlingScoreCalculatorService.calculateBowlingScores(any())).thenReturn(new CalculateScoreResponse());
        ResponseEntity<CalculateScoreResponse> calculateScoreResponseResponseEntity = bowlingScoreController.calculateBowlingScores(calculateScoreRequest);
        Assertions.assertFalse(CollectionUtils.isEmpty(calculateScoreResponseResponseEntity.getBody().getErrors()));
        Assertions.assertEquals(calculateScoreResponseResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
