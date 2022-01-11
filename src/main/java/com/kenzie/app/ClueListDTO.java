package com.kenzie.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Represents a list of 100 ClueDTO objects as retrieved by the API
 * call: https://jservice.kenzie.academy/api/clues.
 *
 * Will be used to randomly select from for the questions for the game.
 *
 * @author Ethan Tauriainen
 */
@JsonPropertyOrder({"clues"})
public class ClueListDTO {

    @JsonProperty("clues")
    private List<ClueDTO> clues;

    public List<ClueDTO> getClues() {
        return clues;
    }

    public void setClues(List<ClueDTO> clues) {
        this.clues = clues;
    }
}
