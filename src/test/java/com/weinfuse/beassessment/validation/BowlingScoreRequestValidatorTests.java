package com.weinfuse.beassessment.validation;

import com.weinfuse.beassessment.bos.CalculateScoreRequest;
import com.weinfuse.beassessment.bos.CalculateScoreResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
public class BowlingScoreRequestValidatorTests {

    @Test
    public void normalInputValidation() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        List<List<String>> frameScores = new ArrayList<>();
        frameScores.add(Arrays.asList("1", "2", "X", "9", "/"));
        frameScores.add(Arrays.asList("2", "/", "1", "4", "X"));
        calculateScoreRequest.setIndividualFrameScores(frameScores);

        CalculateScoreResponse calculateScoreResponse = BowlingScoreRequestValidator.staticValidator.validateBowlingScoreCalculationRequest(calculateScoreRequest);
        Assertions.assertNull(calculateScoreResponse);
    }

    @Test
    public void incorrectInputValidation() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        List<List<String>> frameScores = new ArrayList<>();
        frameScores.add(Arrays.asList("1", "2", "X", "9", "L"));
        calculateScoreRequest.setIndividualFrameScores(frameScores);

        CalculateScoreResponse calculateScoreResponse = BowlingScoreRequestValidator.staticValidator.validateBowlingScoreCalculationRequest(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponse.getErrors().get(0), "Frame scores may only be 0-9, /, and X");
    }

    @Test
    public void emptyInputValidation() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();

        CalculateScoreResponse calculateScoreResponse = BowlingScoreRequestValidator.staticValidator.validateBowlingScoreCalculationRequest(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponse.getErrors().get(0), "Must pass in scores to be tallied");
    }

    @Test
    public void tooManyFramesInputValidation() {
        CalculateScoreRequest calculateScoreRequest = new CalculateScoreRequest();
        List<List<String>> frameScores = new ArrayList<>();
        frameScores.add(Arrays.asList("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"));
        calculateScoreRequest.setIndividualFrameScores(frameScores);

        CalculateScoreResponse calculateScoreResponse = BowlingScoreRequestValidator.staticValidator.validateBowlingScoreCalculationRequest(calculateScoreRequest);
        Assertions.assertEquals(calculateScoreResponse.getErrors().get(0), "A game of bowling cannot be greater than 10-12 frames");
    }

}
