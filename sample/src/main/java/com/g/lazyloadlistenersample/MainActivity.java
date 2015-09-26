package com.g.lazyloadlistenersample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.g.lazyloadlistener.LazyLoadOnScrollListener;
import com.g.lazyloadlistenersample.adapters.BooksAdapter;
import com.g.lazyloadlistenersample.data.Book;
import com.g.lazyloadlistenersample.service.BooksApiService;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BooksApiService apiService;
    private Subscription subscription;

    private BooksAdapter adapter;

    private EditText searchField;
    private ListView booksList;
    private ProgressBar lazyLoadProgress;
    private String currentQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = (EditText) findViewById(R.id.et_query);
        findViewById(R.id.btn_search).setOnClickListener(this);

        booksList = (ListView) findViewById(R.id.lv_books_list);

        View lazyLoadFooter = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_lazy_load, null);
        lazyLoadProgress = (ProgressBar) lazyLoadFooter.findViewById(R.id.pb_progress);
        booksList.addFooterView(lazyLoadFooter, null, false);

        adapter = new BooksAdapter(this);
        booksList.setAdapter(adapter);

        booksList.setOnScrollListener(listener);

        apiService = new BooksApiService();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_search) {
            listener.reset();
            adapter.setBooksList(null);
            adapter.notifyDataSetChanged();
            currentQuery = searchField.getText().toString().trim().replace(" ", "+");
            loadBooks(currentQuery);
        }
    }

    private LazyLoadOnScrollListener listener = new LazyLoadOnScrollListener(BooksApiService.LIMIT) {
        @Override
        public void onLoad(int offset) {
            loadBooks(currentQuery, offset);
        }
    };

    private void loadBooks(String query) {
        loadBooks(query, 0);
    }

    private void loadBooks(String query, int offset) {

        lazyLoadProgress.setVisibility(View.VISIBLE);

        subscription = apiService
                .searchForBook(query, offset)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Book>>() {

                    @Override
                    public void onCompleted() {
                        hideSoftKeyboard();
                        lazyLoadProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideSoftKeyboard();
                        lazyLoadProgress.setVisibility(View.GONE);
                        listener.notifyFail();
                        showFailedNotification();
                    }

                    @Override
                    public void onNext(List<Book> books) {
                        listener.notifyFinish(books.size());
                        adapter.getBooksList().addAll(books);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void showFailedNotification() {

        Snackbar.make(booksList, "Unable to load data", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadBooks(currentQuery, listener.getLoadedCount());
                    }
                }).show();

    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
