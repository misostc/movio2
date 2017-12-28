package cz.muni.fi.pv256.movio2.uco_410434.service;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_410434.model.Film;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDBService {
    String API_DATE_FORMAT = "yyyy-MM-dd";

    @GET("discover/movie?"
            + "sort_by=vote_average.desc"
            + "&include_adult=false"
            + "&include_video=false"
            + "&page=1")
    Call<List<Film>> getTopRated(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("discover/movie?"
            + "sort_by=vote_average.desc"
            + "&include_adult=false"
            + "&include_video=false"
            + "&page=1")
    Call<List<Film>> getTopRatedSince(@Query("api_key") String apiKey, @Query("language") String language, @Query("release_date.gte") String dateThreshold, @Query("release_date.lte") String todayDate);

    @GET("movie/{filmId}")
    Call<Film> getFilmDetails(@Path("filmId") Long id, @Query("api_key") String apiKey, @Query("language") String language);
}
