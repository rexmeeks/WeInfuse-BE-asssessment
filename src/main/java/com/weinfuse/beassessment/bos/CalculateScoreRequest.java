package com.weinfuse.beassessment.bos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CalculateScoreRequest {
    private List<List<String>> individualFrameScores;
}
