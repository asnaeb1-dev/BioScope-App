package com.example.bioscope.POJO.TMDBAPI;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NowPlaying {

    @SerializedName("results")
    @Expose
    private List<Result> results = null;


    public List<Result> getResults() {
        return results;
    }

    public class Result {

        @SerializedName("poster_path")
        @Expose
        private String posterPath;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("backdrop_path")
        @Expose
        private String backdropPath;
        @SerializedName("original_title")
        @Expose
        private String originalTitle;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("vote_average")
        @Expose
        private Double voteAverage;


        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }


        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }
    }
}