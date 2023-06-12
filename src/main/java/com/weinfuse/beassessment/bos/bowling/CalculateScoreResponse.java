package com.weinfuse.beassessment.bos.bowling;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CalculateScoreResponse {

    private List<List<Integer>> calculatedScores = new ArrayList<>();

}
