package com.g.lazyloadlistenersample.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.g.lazyloadlistenersample.R;
import com.g.lazyloadlistenersample.data.Book;
import com.g.lazyloadlistenersample.data.VolumeInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends BaseAdapter {

    private List<Book> books = new ArrayList<>();

    private Context context;
    private LayoutInflater inflater;

    public BooksAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Book getItem(int i) {
        return books.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        VolumeInfo book = getItem(i).getVolumeInfo();

        ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.item_book, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name.setText(book.getTitleStrings());
        viewHolder.author.setText(book.getAuthorsString());
        viewHolder.date.setText(book.getPublishDateString());

        Picasso.with(context)
                .load(book.getThumbnail())
                .placeholder(android.R.color.darker_gray)
                .into(viewHolder.image);

        return view;
    }

    public List<Book> getBooksList() {
        return books;
    }

    public void setBooksList(@Nullable List<Book> books) {
        this.books = books == null ? new ArrayList<Book>() : books;
    }

    private static class ViewHolder {

        public ImageView image;
        public TextView name;
        public TextView author;
        public TextView date;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.iv_image);
            name = (TextView) view.findViewById(R.id.tv_name);
            author = (TextView) view.findViewById(R.id.tv_author);
            date = (TextView) view.findViewById(R.id.tv_pub_date);
        }
    }
}
