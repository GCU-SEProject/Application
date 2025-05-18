package com.example.setp.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.setp.R;

import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private Context context;
    private List<Game> gameList;

    public GameAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_game_result, parent, false); // Using list_item_search_result.xml
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (gameList == null || gameList.isEmpty() || position >= gameList.size()) {
            return;
        }

        Game item = gameList.get(position);

        holder.tvItemTitle.setText(item.getName());
        String companyAndRelease = item.getCompany() + " | " + item.getReleaseDate();
        holder.tvItemCompanyAndRelease.setText(companyAndRelease);

        if (item.getPrice() > 0) {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.KOREA);
            holder.tvItemPrice.setText(currencyFormat.format(item.getPrice()));
            holder.tvItemPrice.setVisibility(View.VISIBLE);
        } else {
            holder.tvItemPrice.setText(context.getString(R.string.free_to_play));
        }

        holder.tvItemGameId.setText("ID: " + item.getId());
        holder.tvItemAgeRating.setText("이용 등급: " + item.getAgeRating() + "세 이용가");
        holder.tvItemLink.setText("링크: " + item.getLink());
        holder.tvItemFullDescription.setText(item.getDescription());

        boolean isExpanded = item.isExpanded();
        holder.llDetailInfo.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.llItemContainer.setOnClickListener(v -> {
            item.setExpanded(!isExpanded);
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return gameList == null ? 0 : gameList.size();
    }

    public void updateData(List<Game> newGameList) {

        for (Game game : newGameList) {
            game.setExpanded(false);
        }

        this.gameList.clear();
        if (newGameList != null) {
            this.gameList.addAll(newGameList);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItemContainer;
        LinearLayout llSummaryInfo;
        LinearLayout llDetailInfo;

        TextView tvItemTitle;
        TextView tvItemCompanyAndRelease; // ID 변경된 TextView
        TextView tvItemPrice;

        TextView tvItemGameId;
        TextView tvItemAgeRating;
        TextView tvItemLink;
        TextView tvItemFullDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llItemContainer = itemView.findViewById(R.id.llItemContainer);
            llSummaryInfo = itemView.findViewById(R.id.llSummaryInfo);
            llDetailInfo = itemView.findViewById(R.id.llDetailInfo);

            tvItemTitle = itemView.findViewById(R.id.tvItemTitle);
            tvItemCompanyAndRelease = itemView.findViewById(R.id.tvItemCompanyAndRelease);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);

            tvItemGameId = itemView.findViewById(R.id.tvItemGameId);
            tvItemAgeRating = itemView.findViewById(R.id.tvItemAgeRating);
            tvItemLink = itemView.findViewById(R.id.tvItemLink);
            tvItemFullDescription = itemView.findViewById(R.id.tvItemFullDescription);
        }
    }
}