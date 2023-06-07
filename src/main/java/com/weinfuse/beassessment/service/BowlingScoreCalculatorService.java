package com.weinfuse.beassessment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BowlingScoreCalculatorService {


    // was thinking about doing like tuples, i.e. split up the array in a manner that corresponds with frame calculations
    // like if [1, /, 3, 6, X, 8, 1] -> [1, /, 3], [3,6], [X, 8, 1], [8, 1] -> [13, 9, 19, 9]
    // but I thought
    public List<Integer> calculateBowlingScores(List<String> frameScores) {
        List<Integer> calculatedScores = new ArrayList<>();
        Integer frame = 0;
        Integer carriedScore = null;
        Boolean strike = false;

        List<List<String>> temp = new ArrayList<>();


        for(int i = 0; i < frameScores.size(); i++) {
            String currentFrame = frameScores.get(i);
            if(currentFrame.equals("X") || currentFrame.equals("/")) {
                log.error("ehh");
            } else {
                try {
                    Integer score = Integer.parseInt(currentFrame);
                    if(carriedScore != null) {
                        if(carriedScore < 10) {
                            calculatedScores.add(carriedScore + score);
                            carriedScore = null;
                        } else if (!strike){
                            carriedScore += score;
                            calculatedScores.add(carriedScore);
                            carriedScore = score;
                        } else {
                            carriedScore += score;
                            strike = false;
                        }
                    } else {
                        carriedScore += score;
                    }
                } catch (NumberFormatException e) {
                    // todo error handling
                    log.error("Someone scored a value that is impossible");
                }
            }
        }

        return calculatedScores;
    }
}
