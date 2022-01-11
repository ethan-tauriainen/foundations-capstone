package com.kenzie.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents an individual clue as retrieved from the API:
 * https://jservice.kenzie.academy
 *
 * Contains two nested classes: Category and Game.
 *
 * @author Ethan Tauriainen
 */
@JsonPropertyOrder({"id", "answer", "question", "value", "categoryId", "gameId", "invalidCount", "category", "game", "canon"})
public class ClueDTO {

    @JsonProperty("id")
    private long id;
    @JsonProperty("answer")
    private String answer;
    @JsonProperty("question")
    private String question;
    @JsonProperty("value")
    private long value;
    @JsonProperty("categoryId")
    private long categoryId;
    @JsonProperty("gameId")
    private long gameId;
    @JsonProperty("invalidCount")
    private long invalidCount;
    @JsonProperty("category")
    private Category category;
    @JsonProperty("game")
    private Game game;
    @JsonProperty("canon")
    private boolean canon;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getInvalidCount() {
        return invalidCount;
    }

    public void setInvalidCount(long invalidCount) {
        this.invalidCount = invalidCount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean getCanon() {
        return canon;
    }

    public void setCanon(boolean canon) {
        this.canon = canon;
    }

    @Override
    public String toString() {
        return "ClueDTO{" +
                "id=" + id +
                ", answer='" + answer + '\'' +
                ", question='" + question + '\'' +
                ", value=" + value +
                ", categoryId=" + categoryId +
                ", invalidCount=" + invalidCount +
                ", category=" + category.toString() +
                ", game=" + game.toString() +
                ", canon=" + canon +
                '}';
    }

    /**
     * Represents the category object contained within each clue.
     */
    @JsonPropertyOrder({"id", "title", "canon"})
    static class Category {
        @JsonProperty("id")
        private long id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("canon")
        private boolean canon;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean getCanon() {
            return canon;
        }

        public void setCanon(boolean canon) {
            this.canon = canon;
        }

        @Override
        public String toString() {
            return "Category{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", canon=" + canon +
                    '}';
        }
    }

    /**
     * Represents the game object contained within each clue.
     */
    @JsonPropertyOrder({"aired", "canon"})
    static class Game {
        @JsonProperty("aired")
        private String aired;
        @JsonProperty("canon")
        private boolean canon;

        public String getAired() {
            return aired;
        }

        public void setAired(String aired) {
            this.aired = aired;
        }

        public boolean getCanon() {
            return canon;
        }

        public void setCanon(boolean canon) {
            this.canon = canon;
        }

        @Override
        public String toString() {
            return "Game{" +
                    "aired='" + aired + '\'' +
                    ", canon=" + canon +
                    '}';
        }
    }
}
