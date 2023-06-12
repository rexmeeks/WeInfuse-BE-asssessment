package com.weinfuse.beassessment.validation;

import com.weinfuse.beassessment.bos.CalculateScoreRequest;
import com.weinfuse.beassessment.bos.CalculateScoreResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BowlingScoreRequestValidator {

    public static class staticValidator {
        // usually for larger system validation, I'd usually either throw a custom error here that would generate the http response back
        // and then either have an interceptor that handles and returns those, or handle the throw in wherever you call the validation
        // in this case I'm just going to return a string to keep it simple and in the same vein that's why I'm not putting the messages
        // into a constants class
        public static CalculateScoreResponse validateBowlingScoreCalculationRequest(CalculateScoreRequest calculateScoreRequest) {
            CalculateScoreResponse calculateScoreResponse = new CalculateScoreResponse();
            List<String> errors = new ArrayList<>();

            if (calculateScoreRequest == null || calculateScoreRequest.equals(new CalculateScoreRequest()) || CollectionUtils.isEmpty(calculateScoreRequest.getIndividualFrameScores())) {
                errors.add("Must pass in scores to be tallied");
                calculateScoreResponse.setErrors(errors);
                return calculateScoreResponse;
            }

            List<String> playerScoreErrors = calculateScoreRequest.getIndividualFrameScores().stream().map(playerScores -> {
                if (CollectionUtils.isEmpty(playerScores)) {
                    return "Must pass in scores to be tallied";
                }

                String scores = StringUtils.join(playerScores, "");

                // if the size doesn't match up that means someone used a 2 digit number and that is wrong
                if (!scores.matches("^[0-9/X]*$") || scores.length() != playerScores.size()) {
                    return "Frame scores may only be 0-9, /, and X";
                }

                if (playerScores.stream().mapToDouble(score -> {
                    if(score.equalsIgnoreCase("X")) {
                        return 1;
                    } else {
                        return .5;
                    }
                }).sum() > 12) {
                    return "A game of bowling cannot be greater than 10-12 frames";
                }

                return null;
            }).collect(Collectors.toList());

            // only return the playerScoreErrors if it's non empty for at least one player
            if(playerScoreErrors.stream().anyMatch(Objects::nonNull)) {
                calculateScoreResponse.setErrors(playerScoreErrors);
                return calculateScoreResponse;
            }

            return null;
        }
    }

}
