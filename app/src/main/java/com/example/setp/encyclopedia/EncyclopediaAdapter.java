package com.example.setp.encyclopedia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.setp.R;
import java.util.List;

public class EncyclopediaAdapter extends RecyclerView.Adapter<EncyclopediaAdapter.ViewHolder> {

    private Context context;
    private List<Encyclopedia> encyclopediaList;

    // Constructor
    public EncyclopediaAdapter(Context context, List<Encyclopedia> encyclopediaList) {
        this.context = context;
        this.encyclopediaList = encyclopediaList;
    }

    // Inflate item layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_encyclopedia_result, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (encyclopediaList == null || encyclopediaList.isEmpty() || position >= encyclopediaList.size()) {
            return;
        }

        Encyclopedia item = encyclopediaList.get(position);

        // Set HTML text depending on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvEncyclopediaTitle.setText(Html.fromHtml(item.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            holder.tvEncyclopediaDescription.setText(Html.fromHtml(item.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.tvEncyclopediaTitle.setText(Html.fromHtml(item.getTitle()));
            holder.tvEncyclopediaDescription.setText(Html.fromHtml(item.getDescription()));
        }

        // Load thumbnail image
        Glide.with(context)
                .load(item.getThumbnail())
                .placeholder(R.drawable.ic_placeholder_image)
                .error(R.drawable.ic_placeholder_image)
                .into(holder.ivEncyclopediaThumbnail);

        // Set link text
        holder.tvEncyclopediaLink.setText("링크: " + item.getLink());

        // Show or hide detail section
        boolean isExpanded = item.isExpanded();
        holder.llEncyclopediaDetailInfo.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Toggle detail view when item is clicked
        holder.llEncyclopediaItemContainer.setOnClickListener(v -> {
            item.setExpanded(!isExpanded);
            notifyItemChanged(holder.getAdapterPosition());
        });

        // Open link in browser when link text is clicked
        holder.tvEncyclopediaLink.setOnClickListener(v -> {
            if (item.getLink() != null && !item.getLink().isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                if (browserIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(browserIntent);
                }
            }
        });
    }

    // Return number of items
    @Override
    public int getItemCount() {
        return encyclopediaList == null ? 0 : encyclopediaList.size();
    }

    // Update adapter data
    public void updateData(List<Encyclopedia> newEncyclopediaList) {
        if (newEncyclopediaList != null) {
            for (Encyclopedia encyclopedia : newEncyclopediaList) {
                encyclopedia.setExpanded(false); // Collapse all items
            }
        }
        this.encyclopediaList.clear();
        if (newEncyclopediaList != null) {
            this.encyclopediaList.addAll(newEncyclopediaList);
        }
        notifyDataSetChanged();
    }

    // Holds references to views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llEncyclopediaItemContainer, llEncyclopediaSummaryInfo, llEncyclopediaDetailInfo;
        ImageView ivEncyclopediaThumbnail;
        TextView tvEncyclopediaTitle, tvEncyclopediaDescription, tvEncyclopediaLink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llEncyclopediaItemContainer = itemView.findViewById(R.id.llEncyclopediaItemContainer);
            llEncyclopediaSummaryInfo = itemView.findViewById(R.id.llEncyclopediaSummaryInfo);
            llEncyclopediaDetailInfo = itemView.findViewById(R.id.llEncyclopediaDetailInfo);
            ivEncyclopediaThumbnail = itemView.findViewById(R.id.ivEncyclopediaThumbnail);
            tvEncyclopediaTitle = itemView.findViewById(R.id.tvEncyclopediaTitle);
            tvEncyclopediaDescription = itemView.findViewById(R.id.tvEncyclopediaDescription);
            tvEncyclopediaLink = itemView.findViewById(R.id.tvEncyclopediaLink);
        }
    }
}