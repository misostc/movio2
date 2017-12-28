package cz.muni.fi.pv256.movio2.uco_410434.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_410434.model.Film;
import cz.muni.fi.pv256.movio2.uco_410434.model.FilmWrapper;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDBServiceUtil {
    @NonNull
    public static MovieDBService createService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(new Converter.Factory() {
                    @Nullable
                    @Override
                    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                        if (List.class.equals(getRawType(type))) {
                            return new Converter<ResponseBody, List<Film>>() {
                                @Override
                                public List<Film> convert(@NonNull ResponseBody value) throws IOException {
                                    FilmWrapper resultWrapper = getGson().fromJson(value.charStream(), FilmWrapper.class);
                                    value.close();
                                    return resultWrapper.getResults();
                                }

                            };
                        } else {
                            return null;
                        }
                    }
                })
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();
        return retrofit.create(MovieDBService.class);
    }

    @NonNull
    private static Gson getGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}
