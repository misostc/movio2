package cz.muni.fi.pv256.movio2.uco_410434.manager;

import com.google.gson.Gson;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_410434.model.Film;

public class JSONFilmMapper {

    private static String IMAGE_BASE_URI = "http://image.tmdb.org/t/p/";
    private static String IMAGE_SIZE = "w780";

    public static List<Film> convertFromJson(Reader response) {
        MovieDBResponse dbResponse = new Gson().fromJson(response, MovieDBResponse.class);
        return mapFilms(dbResponse);
    }

    private static List<Film> mapFilms(MovieDBResponse dbResponse) {
        if (dbResponse.results == null || dbResponse.results.isEmpty()) {
            return Collections.emptyList();
        }

        List<Film> resultList = new ArrayList<>();
        for (MovieDBResponse.Result result : dbResponse.results) {
            resultList.add(new Film(result.title, result.vote_average, buildImageUri(result.backdrop_path)));
        }
        return resultList;
    }

    private static String buildImageUri(String backdrop_path) {
        if (backdrop_path == null) {
            return null;
        }
        return String.format("%s/%s/%s", IMAGE_BASE_URI, IMAGE_SIZE, backdrop_path);
    }

    static class MovieDBResponse {
        List<Result> results;

        static class Result {
            String title;
            Double vote_average;
            String backdrop_path;
        }
    }
}
