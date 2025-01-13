package com.example.finelspruject.adapters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finelspruject.Book;
import com.example.finelspruject.R;

import java.util.List;

public class BookAdapter extends BaseAdapter {

    private Context context;
    private List<Book> bookList;
    private LayoutInflater inflater;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.book_item, parent, false);
        }

        // Get book details
        Book book = bookList.get(position);

        // Find views
        ImageView imgBookCover = convertView.findViewById(R.id.imgBookCover);
        TextView txtBookTitle = convertView.findViewById(R.id.txtBookTitle);
        TextView txtBookAuthor = convertView.findViewById(R.id.txtBookAuthor);

        // Set book details
        txtBookTitle.setText(book.getTitle());
        txtBookAuthor.setText(book.getAuthor());

        // Decode and display the book cover
        if (book.getIsbn() != null && !book.getIsbn().isEmpty()) {
            Bitmap bitmap = decodeBase64ToBitmap(book.getIsbn());
            imgBookCover.setImageBitmap(bitmap);
        } else {
            imgBookCover.setImageResource(R.drawable.default_book_cover); // Fallback image
        }

        return convertView;
    }

    // Helper method to decode Base64 string into Bitmap
    private Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
