package com.example.finelspruject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finelspruject.R;
import com.example.finelspruject.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;

    // Constructor
    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.txtBookTitle.setText(book.getTitle());
        holder.txtBookAuthor.setText("Author: " + book.getAuthor());
        holder.txtBookISBN.setText("ISBN: " + book.getIsbn());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView txtBookTitle, txtBookAuthor, txtBookISBN;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookTitle = itemView.findViewById(R.id.txtBookTitle);
            txtBookAuthor = itemView.findViewById(R.id.txtBookAuthor);
            txtBookISBN = itemView.findViewById(R.id.txtBookISBN);
        }
    }
}
