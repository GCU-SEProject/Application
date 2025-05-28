package com.example.setp.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.setp.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static final String TAG_ADAPTER = "NewsAdapter";
    private Context context;
    private List<News> newsList;
    private SimpleDateFormat inputDateFormat;
    private SimpleDateFormat outputDateFormat;

    // Constructor to initialize context and news list
    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;

        // Setup date formats
        inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    }

    // Inflate layout and create ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_news_result, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to each item view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (newsList == null || newsList.isEmpty() || position >= newsList.size()) {
            return;
        }

        News item = newsList.get(position);

        // Set title and description (support HTML)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvNewsTitle.setText(Html.fromHtml(item.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            holder.tvNewsDescription.setText(Html.fromHtml(item.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.tvNewsTitle.setText(Html.fromHtml(item.getTitle()));
            holder.tvNewsDescription.setText(Html.fromHtml(item.getDescription()));
        }

        // Set date and links
        holder.tvNewsPubDate.setText(formatPubDate(item.getPubDate()));
        holder.tvNewsLink.setText("기사 링크: " + item.getLink());
        holder.tvNewsOriginalLink.setText("원본 링크: " + item.getOriginalLink());

        // Clear and add tags if available
        holder.llNewsTagsContainer.removeAllViews();
        if (item.getTags() != null && !item.getTags().isEmpty()) {
            holder.llNewsTagsContainer.setVisibility(View.VISIBLE);
            for (String tag : item.getTags()) {
                TextView tagTextView = new TextView(context);

                // Set layout and style
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMarginEnd((int) (4 * context.getResources().getDisplayMetrics().density));
                tagTextView.setLayoutParams(params);
                tagTextView.setText(tag);
                tagTextView.setTextSize(10);
                tagTextView.setPadding(
                        (int) (6 * context.getResources().getDisplayMetrics().density),
                        (int) (2 * context.getResources().getDisplayMetrics().density),
                        (int) (6 * context.getResources().getDisplayMetrics().density),
                        (int) (2 * context.getResources().getDisplayMetrics().density)
                );
                tagTextView.setBackgroundResource(R.drawable.tag_background_news);
                tagTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.llNewsTagsContainer.addView(tagTextView);
            }
        } else {
            holder.llNewsTagsContainer.setVisibility(View.GONE);
        }

        // Expand/collapse detail info
        boolean isExpanded = item.isExpanded();
        holder.llNewsDetailInfo.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Toggle expand state when item is clicked
        holder.llNewsItemContainer.setOnClickListener(v -> {
            item.setExpanded(!isExpanded);
            notifyItemChanged(holder.getAdapterPosition());
        });

        // Open browser when link is clicked
        View.OnClickListener linkClickListener = v -> {
            String urlToOpen = item.getLink();
            if (urlToOpen == null || urlToOpen.isEmpty()) {
                urlToOpen = item.getOriginalLink();
            }
            if (urlToOpen != null && !urlToOpen.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen));
                if (browserIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(browserIntent);
                } else {
                    Log.w(TAG_ADAPTER, "No activity found to handle URL: " + urlToOpen);
                    Toast.makeText(context, "링크를 열 수 있는 앱이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "기사 URL 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        };
        holder.tvNewsLink.setOnClickListener(linkClickListener);
        holder.tvNewsOriginalLink.setOnClickListener(linkClickListener);
    }

    // Return item count
    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    // Update adapter with new data
    public void updateData(List<News> newNewsList) {
        if (newNewsList != null) {
            for (News news : newNewsList) {
                news.setExpanded(false);
            }
        }
        this.newsList.clear();
        if (newNewsList != null) {
            this.newsList.addAll(newNewsList);
        }
        notifyDataSetChanged();
    }

    // Convert date string to display format
    private String formatPubDate(String pubDateString) {
        if (pubDateString == null || pubDateString.isEmpty()) {
            return "날짜 정보 없음";
        }
        try {
            Date date = inputDateFormat.parse(pubDateString);
            if (date != null) {
                return outputDateFormat.format(date);
            }
        } catch (ParseException e) {
            Log.e(TAG_ADAPTER, "Error parsing pubDate: " + pubDateString, e);
            return pubDateString;
        }
        return pubDateString;
    }

    // ViewHolder to hold item views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llNewsItemContainer, llNewsDetailInfo;
        TextView tvNewsTitle, tvNewsPubDate, tvNewsDescription, tvNewsLink, tvNewsOriginalLink;
        LinearLayout llNewsTagsContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llNewsItemContainer = itemView.findViewById(R.id.llNewsItemContainer);
            llNewsDetailInfo = itemView.findViewById(R.id.llNewsDetailInfo);
            tvNewsTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvNewsPubDate = itemView.findViewById(R.id.tvNewsPubDate);
            tvNewsDescription = itemView.findViewById(R.id.tvNewsDescription);
            tvNewsLink = itemView.findViewById(R.id.tvNewsLink);
            tvNewsOriginalLink = itemView.findViewById(R.id.tvNewsOriginalLink);
            llNewsTagsContainer = itemView.findViewById(R.id.llNewsTagsContainer);
        }
    }
}