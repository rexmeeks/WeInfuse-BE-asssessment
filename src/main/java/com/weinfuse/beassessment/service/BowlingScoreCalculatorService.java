package com.weinfuse.beassessment.service;

import com.weinfuse.beassessment.bos.CalculateScoreRequest;
import com.weinfuse.beassessment.bos.CalculateScoreResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class BowlingScoreCalculatorService {


    /*
     * Since this is a bowling score calculator I will be operating under the assumption that the most frames that will be sent
     * and calculated will be up to 13, as a full game is 13 frames.
     */
    public CalculateScoreResponse calculateBowlingScores(CalculateScoreRequest calculateScoreRequest) {

        CalculateScoreResponse calculateScoreResponse = new CalculateScoreResponse();
        AtomicInteger playerNumber = new AtomicInteger(1);

        log.info("In Bowling Calculator Service");

        calculateScoreRequest.getIndividualFrameScores().forEach(playerScore -> {
                log.info("Calculating score for player {}", playerNumber.get());
                calculateScoreResponse.getCalculatedScores().add(calculateIndividualsFrames(playerScore));
                playerNumber.getAndIncrement();
        });

        return calculateScoreResponse;
    }

    private List<Integer> calculateIndividualsFrames(List<String> frameScores) {
        List<List<Integer>> scoringTuples = new ArrayList<>();
        List<Integer> currentScores = new ArrayList<>();
        List<Integer> nextScores = new ArrayList<>();
        AtomicInteger increment = new AtomicInteger(0);

        log.info("Starting frame calculations for: {}", frameScores.toString());

        /*
         * essentially breaking everything in to tuples, so if you have [9, /, 7, 2, X, X, X, 5, 4]
         * you'll end up with this [[9, 1, 7], [7, 2], [10, 10, 10], [10, 10, 5], [10, 5, 4], [5, 4]]
         * and then you sum up each tuple
         * [17, 9, 30, 25, 19, 9]
         * this is probably the ideal solution; but there was a period where the fact I bowl so much made me start implementing the scoring
         * in basic "human" calculation where I'd mark something as a strike, and then add the next two, or if it's a spare add the next one
         * Not necessarily bad, just kind of a bland algorithm and doesn't demonstrate the relations between individual frames as well
         * Way easier to understand plainly to people who aren't as tech focused, but I think this is much more "clean" and fun to code
         */
        frameScores.forEach(s -> {

            // knowing bowling is at max 12 frames, I figured memory wouldn't be an issue in this case,
            // which is why I'm just copying arrays (aside from obviously preserving data and making this easier in terms of building out tuples)
            Integer score;

            if(s.equalsIgnoreCase("X")) {
                // if it's a strike
                score = 10;
                switch (currentScores.size()) {
                    case 1 -> {
                        currentScores.add(10);
                        nextScores.add(10);

                    }
                    case 2 -> {
                        currentScores.add(10);
                        scoringTuples.add(new ArrayList<>(currentScores));
                        currentScores.clear();
                        currentScores.addAll(new ArrayList<>(nextScores));
                        currentScores.add(10);
                    }
                    default -> currentScores.add(10);
                }
            } else if(s.equals("/")) {
                // if it's a spare
                if(currentScores.size() == 2) {
                    // edge case for final frame
                    score = 10 - currentScores.get(1);
                } else {
                    score = 10 - currentScores.get(0);
                }

                currentScores.add(score);

                if(!CollectionUtils.isEmpty(nextScores)) {
                    nextScores.add(score);
                }
            } else {
                // the case where the score is a number
                score = Integer.parseInt(s);
                if (currentScores.size() == 2 && currentScores.get(0) + currentScores.get(1) < 10){
                    scoringTuples.add(new ArrayList<>(currentScores));
                    currentScores.clear();
                    nextScores.clear();
                }

                currentScores.add(score);

                if(currentScores.get(0) == 10) {
                    nextScores.add(score);
                }
                if(currentScores.size() == 2 && currentScores.get(0) + score < 10) {
                    scoringTuples.add(new ArrayList<>(currentScores));
                    currentScores.clear();
                    nextScores.clear();
                }
            }

            // the big part that decides when a roll is complete, this will be in the cases of strikes and spares, otherwise
            // it'll be handled above
            if(currentScores.size() == 3) {
                scoringTuples.add(new ArrayList<>(currentScores));
                currentScores.clear();
                if(!CollectionUtils.isEmpty(nextScores)) {
                    currentScores.addAll(new ArrayList<>(nextScores));
                    nextScores.clear();
                    if(!(s.equalsIgnoreCase("X") || s.equals("/")) && currentScores.get(0) == 10) {
                        nextScores.add(score);
                    }
                    if(scoringTuples.size() < 9 && increment.get() == frameScores.size() - 1) {
                        scoringTuples.add(new ArrayList<>(currentScores));
                    }
                } else {
                    currentScores.add(score);
                }
            }

            // case where a full game isn't passed in and a frame can't be scored yet
            if(scoringTuples.size() < 10 && increment.get() == frameScores.size() - 1) {
                if (!CollectionUtils.isEmpty(currentScores)) {
                    scoringTuples.add(null);
                }
                // have to check the number of scores already calculated to make sure not pulling next score values that wouldn't come in,
                // in the case of a strike happening in the final frame (since those don't take the sums of the next throws)
                if(scoringTuples.size() < 9 && !CollectionUtils.isEmpty(nextScores)) {
                    scoringTuples.add(null);
                }
            }

            increment.getAndIncrement();
        });

        return sumTuples(scoringTuples);
    }

    private List<Integer> sumTuples(List<List<Integer>> tupleList) {

        log.info("Summing up the tuples now");

        List<Integer> scores = new ArrayList<>();
        tupleList.forEach(tuple -> {
            if(tuple != null) {
                scores.add(tuple.stream().mapToInt(i -> i).sum());
            } else {
                scores.add(null);
            }
        });
        return scores;
    }
}
