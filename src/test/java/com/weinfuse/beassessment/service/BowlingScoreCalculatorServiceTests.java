package com.weinfuse.beassessment.service;

import com.weinfuse.beassessment.bos.CalculateScoreRequest;
import com.weinfuse.beassessment.bos.CalculateScoreResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class BowlingScoreCalculatorServiceTests {

    @Autowired
    private BowlingScoreCalculatorService bowlingScoreCalculatorService;

    @Test
    public void normalRequestTest() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        calculateScoreRequest.setIndividualFrameScores(Arrays.asList(Arrays.asList("9", "/", "7", "2", "X", "X", "0", "6", "8", "/", "5", "/", "7", "1", "X", "X", "9", "0")));
        CalculateScoreResponse calculateScoreResponse = bowlingScoreCalculatorService.calculateBowlingScores(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponse.getCalculatedScores().get(0), Arrays.asList(17, 9, 20, 16, 6, 15, 17, 8, 29, 19));
    }

    @Test
    public void shortGameRequestTest() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        calculateScoreRequest.setIndividualFrameScores(Arrays.asList(Arrays.asList("9", "/", "7", "2", "X", "X", "0", "6", "8", "/", "5", "/", "7", "1", "X", "X", "9")));
        CalculateScoreResponse calculateScoreResponse = bowlingScoreCalculatorService.calculateBowlingScores(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponse.getCalculatedScores().get(0), Arrays.asList(17, 9, 20, 16, 6, 15, 17, 8, 29, null));
    }

    @Test
    public void allStrikesRequestTest() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        calculateScoreRequest.setIndividualFrameScores(Arrays.asList(Arrays.asList("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")));
        CalculateScoreResponse calculateScoreResponse = bowlingScoreCalculatorService.calculateBowlingScores(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponse.getCalculatedScores().get(0), Arrays.asList(30, 30, 30, 30, 30, 30, 30, 30, 30, 30));
    }

    @Test
    public void allStrikesButLastRequestTest() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        calculateScoreRequest.setIndividualFrameScores(Arrays.asList(Arrays.asList("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "8")));
        CalculateScoreResponse calculateScoreResponse = bowlingScoreCalculatorService.calculateBowlingScores(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponse.getCalculatedScores().get(0), Arrays.asList(30, 30, 30, 30, 30, 30, 30, 30, 30, 28));
    }

    @Test
    public void allStrikesNotFinishedRequestTest() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        calculateScoreRequest.setIndividualFrameScores(Arrays.asList(Arrays.asList("X", "X", "X", "X", "X", "X", "X", "X", "X", "X")));
        CalculateScoreResponse calculateScoreResponse = bowlingScoreCalculatorService.calculateBowlingScores(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponse.getCalculatedScores().get(0), Arrays.asList(30, 30, 30, 30, 30, 30, 30, 30, null));
    }

    @Test
    public void normalDoubleScoreRequestTest() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        List<List<String>> frameScores = new ArrayList<>();
        frameScores.add(Arrays.asList("9", "/", "7", "2", "X", "X", "0", "6", "8", "/", "5", "/", "7", "1", "X", "X", "9", "0"));
        frameScores.add(Arrays.asList("4", "/", "2", "2", "X", "X", "1", "6", "8", "1", "X", "7", "1", "X", "X", "1", "0"));
        calculateScoreRequest.setIndividualFrameScores(frameScores);
        CalculateScoreResponse calculateScoreResponse = bowlingScoreCalculatorService.calculateBowlingScores(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponse.getCalculatedScores(), Arrays.asList(Arrays.asList(17, 9, 20, 16, 6, 15, 17, 8, 29, 19), Arrays.asList(12, 4, 21, 17, 7, 9, 18, 18, 21, 11)));
    }
}
