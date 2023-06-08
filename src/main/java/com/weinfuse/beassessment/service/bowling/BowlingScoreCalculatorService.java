package com.weinfuse.beassessment.service.bowling;

import com.weinfuse.beassessment.bos.bowling.CalculateScoreRequest;
import com.weinfuse.beassessment.bos.bowling.CalculateScoreResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BowlingScoreCalculatorService {


    // knowing bowling is only 13 frames, I figured memory wouldn't be an issue in this case,
    // which is why I'm just copying arrays (aside from obviously preserving data and making this easier in terms of building out tuples)
    public CalculateScoreResponse calculateBowlingScores(CalculateScoreRequest calculateScoreRequest) {
        List<String> frameScores = calculateScoreRequest.getIndividualFrameScores();
        List<Integer> calculatedScores = new ArrayList<>();

        List<List<Integer>> temp = new ArrayList<>();
        List<Integer> currentScores = new ArrayList<>();
        List<Integer> nextScores = new ArrayList<>();
        AtomicInteger increment = new AtomicInteger(0);

        frameScores.forEach(s -> {
            if(s.equalsIgnoreCase("X")) {
                // shouldn't have to do this, but for understanding sake, because nextScores will be [10]
                switch (currentScores.size()) {
                    case 1 -> {
                        currentScores.add(10);
                        nextScores.add(10);
                    }
                    case 2 -> {
                        currentScores.add(10);
                        temp.add(new ArrayList<>(currentScores));
                        currentScores.clear();
                        currentScores.addAll(new ArrayList<>(nextScores));
                        currentScores.add(10);
                        nextScores.clear();
                        nextScores.add(10);
                    }
                    default -> currentScores.add(10);
                }
            } else if(s.equalsIgnoreCase("/")) {
                currentScores.add(10 - currentScores.get(0));
            } else {
                currentScores.add(Integer.parseInt(s));
                if(currentScores.get(0) == 10) {
                    nextScores.add(Integer.parseInt(s));
                }
                if(currentScores.size() == 2 && currentScores.get(0) + Integer.parseInt(s) < 10) {
                    temp.add(new ArrayList<>(currentScores));
                    currentScores.clear();
                    nextScores.clear();
                }
            }

            if(currentScores.size() == 3) {
                temp.add(new ArrayList<>(currentScores));
                currentScores.clear();
                if(!nextScores.isEmpty()) {
                    currentScores.addAll(new ArrayList<>(nextScores));
                    nextScores.clear();
                    if(!(s.equalsIgnoreCase("X") || s.equalsIgnoreCase("/"))) {
                        nextScores.add(Integer.parseInt(s));
                    }
                    if(increment.get() == frameScores.size() - 1) {
                        // todo nextscores has to be complete, otherwise it may be null in the case of strikes
                        temp.add(currentScores);
                    }
                } else {
                    currentScores.add(Integer.parseInt(s));
                }
            }
            increment.getAndIncrement();
        });

        log.info(temp.toString());

//        for(int i = 0; i < frameScores.size(); i++) {
//            String currentFrame = frameScores.get(i);
//            if(currentFrame.equals("X") || currentFrame.equals("/")) {
//                log.error("ehh");
//            } else {
//                try {
//                    Integer score = Integer.parseInt(currentFrame);
//                    if(carriedScore != null) {
//                        if(carriedScore < 10) {
//                            calculatedScores.add(carriedScore + score);
//                            carriedScore = null;
//                        } else if (!strike){
//                            carriedScore += score;
//                            calculatedScores.add(carriedScore);
//                            carriedScore = score;
//                        } else {
//                            carriedScore += score;
//                            strike = false;
//                        }
//                    } else {
//                        carriedScore += score;
//                    }
//                } catch (NumberFormatException e) {
//                    // todo error handling
//                    log.error("Someone scored a value that is impossible");
//                }
//            }
//        }

        CalculateScoreResponse calculateScoreResponse = new CalculateScoreResponse();
        calculateScoreResponse.setCalculatedScores(calculatedScores);
        return calculateScoreResponse;
    }
}
