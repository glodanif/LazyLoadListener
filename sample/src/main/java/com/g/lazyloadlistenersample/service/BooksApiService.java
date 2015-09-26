package com.g.lazyloadlistenersample.service;

import com.g.lazyloadlistenersample.data.Book;
import com.g.lazyloadlistenersample.data.BooksResponse;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.functions.Func1;

public class BooksApiService {

    public static final String MAL_API_HOST = "https://www.googleapis.com/books/v1";

    public static final int LIMIT = 20;

    private final GoogleBooksService apiService;

    public BooksApiService() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MAL_API_HOST)
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(GoogleBooksService.class);
    }

    public Observable<List<Book>> searchForBook(String query, int offset) {

        return apiService.searchForBook(query, offset, LIMIT)
                .map(new Func1<BooksResponse, List<Book>>() {

                    @Override
                    public List<Book> call(final BooksResponse listData) {
                        return listData.getItems();
                    }
                });
    }

    public interface GoogleBooksService {

        @GET("/volumes")
        Observable<BooksResponse> searchForBook(@Query("q") String query, @Query("startIndex") int offset, @Query("maxResults") int limit);

    }
}
