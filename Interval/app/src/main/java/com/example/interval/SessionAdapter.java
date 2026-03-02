package com.example.interval;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM   = 1;

    public interface OnDeleteClickListener {
        void onDelete(int sessionId, int position);
    }

    private final Context context;
    private final List<Object> items;
    private final OnDeleteClickListener deleteListener;

    public SessionAdapter(Context context, List<Object> items, OnDeleteClickListener deleteListener) {
        this.context        = context;
        this.items          = items;
        this.deleteListener = deleteListener;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_HEADER) {
            View v = inflater.inflate(R.layout.item_section_header, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = inflater.inflate(R.layout.item_session, parent, false);
            return new ItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).header.setText((String) items.get(position));

        } else if (holder instanceof ItemViewHolder) {
            SessionListModel session = (SessionListModel) items.get(position);
            ItemViewHolder h = (ItemViewHolder) holder;

            h.txtName.setText(session.getTitle());
            h.txtFocus.setText(session.getFocusTime() + "m");
            h.txtRest.setText(session.getRestTime() + "m");
            h.txtTimeRange.setText(session.getDate());

            // Edit
            h.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, Edit_Session.class);
                intent.putExtra("session_id", session.getId());
                intent.putExtra("title",      session.getTitle());
                intent.putExtra("focus_time", session.getFocusTime());
                intent.putExtra("rest_time",  session.getRestTime());
                context.startActivity(intent);
            });

            // Delete
            h.btnDelete.setOnClickListener(v ->
                    deleteListener.onDelete(session.getId(), holder.getAdapterPosition()));
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }


    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        HeaderViewHolder(View v) {
            super(v);
            header = (TextView) v;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtTimeRange, txtFocus, txtRest;
        ImageView btnEdit, btnDelete;
        ItemViewHolder(View v) {
            super(v);
            txtName      = v.findViewById(R.id.txtSessionName);
            txtTimeRange = v.findViewById(R.id.txtTimeRange);
            txtFocus     = v.findViewById(R.id.txtFocus);
            txtRest      = v.findViewById(R.id.txtRest);
            btnEdit      = v.findViewById(R.id.btnEdit);
            btnDelete    = v.findViewById(R.id.btnDelete);
        }
    }
}