package com.example.finelspruject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finelspruject.LibraryDetailsActivity;
import com.example.finelspruject.R;
import com.example.finelspruject.Library;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {

    private List<Library> libraryList; // List of libraries
    private Context context; // Context to start new activities

    // Constructor
    public LibraryAdapter(List<Library> libraryList, Context context) {
        this.libraryList = libraryList;
        this.context = context;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_item, parent, false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        Library library = libraryList.get(position);
        holder.txtLibraryName.setText(library.getName());
        holder.txtLibraryLocation.setText("Location: " + library.getLocation());

        // Set click listener for each library item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch LibraryDetailsActivity and pass library details
                Intent intent = new Intent(context, LibraryDetailsActivity.class);
                intent.putExtra("library_name", library.getName());
                intent.putExtra("library_location", library.getLocation());
                intent.putExtra("library_id", library.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return libraryList.size();
    }

    // ViewHolder class to hold item views
    public static class LibraryViewHolder extends RecyclerView.ViewHolder {
        TextView txtLibraryName, txtLibraryLocation;

        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLibraryName = itemView.findViewById(R.id.txtLibraryName);
            txtLibraryLocation = itemView.findViewById(R.id.txtLibraryLocation);
        }
    }
}
