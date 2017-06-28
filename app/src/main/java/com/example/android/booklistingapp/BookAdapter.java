package com.example.android.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        // Find the Book at the given position in the list of earthquakes
        Book currentBook = getItem(position);

        String originalTitle = currentBook.getTitle();

        String originalAuthor = currentBook.getmAuthor();


        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(originalTitle);

        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(originalAuthor);

        return listItemView;
    }
}
