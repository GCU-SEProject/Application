package com.example.setp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class GameResultAdapter extends RecyclerView.Adapter<GameResultAdapter.ViewHolder> {

    private Context context;
    private List<Game> gameList;

    public GameResultAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_search_result, parent, false); // Using list_item_search_result.xml
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (gameList != null && !gameList.isEmpty() && position < gameList.size()) {
            Game item = gameList.get(position);

            holder.tvItemTitle.setText(item.getName());
            holder.tvItemDescription.setText(item.getDescription());

            if (item.getPrice() > 0) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.KOREA);
                holder.tvItemPrice.setText(currencyFormat.format(item.getPrice()));
                holder.tvItemPrice.setVisibility(View.VISIBLE);
            } else {
                holder.tvItemPrice.setText(context.getString(R.string.free_to_play));
            }
        }
    }

    @Override
    public int getItemCount() {
        return gameList == null ? 0 : gameList.size();
    }

    public void updateData(List<Game> newGameList) {
        this.gameList.clear();
        if (newGameList != null) {
            this.gameList.addAll(newGameList);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemTitle;
        TextView tvItemDescription;
        TextView tvItemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tvItemTitle);
            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
        }
    }
}