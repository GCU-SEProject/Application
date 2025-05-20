package com.example.setp.video;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.setp.R;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context context;
    private List<Video> videoList;

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_video_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (videoList == null || videoList.isEmpty() || position >= videoList.size()) {
            return;
        }

        Video item = videoList.get(position);

        holder.tvVideoTitle.setText(item.getTitle());
        NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
        holder.tvVideoViewCount.setText("조회수: " + formatter.format(item.getViewCount()) + "회");

        Glide.with(context)
                .load(item.getThumbnailUrl())
                .placeholder(R.drawable.ic_placeholder_image)
                .error(R.drawable.ic_placeholder_image)
                .into(holder.ivVideoThumbnail);

        holder.tvVideoFullDescription.setText(item.getDescription());
        holder.tvVideoUploadTime.setText("업로드: " + formatUploadTime(item.getUploadTime()));

        holder.llVideoTagsContainer.removeAllViews();
        if (item.getTags() != null && !item.getTags().isEmpty()) {
            holder.llVideoTagsContainer.setVisibility(View.VISIBLE);
            for (String tag : item.getTags()) {
                TextView tagTextView = new TextView(context);
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
                tagTextView.setBackgroundResource(R.drawable.tag_background_video);
                tagTextView.setTextColor(ContextCompat.getColor(context, R.color.tag_text_color_video));
                holder.llVideoTagsContainer.addView(tagTextView);
            }
        } else {
            holder.llVideoTagsContainer.setVisibility(View.GONE);
        }

        boolean isExpanded = item.isExpanded();
        holder.llVideoDetailInfo.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.llVideoItemContainer.setOnClickListener(v -> {
            item.setExpanded(!isExpanded);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.ivVideoThumbnail.setOnClickListener(v -> {
            if (item.getVideoUrl() != null && !item.getVideoUrl().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getVideoUrl()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList == null ? 0 : videoList.size();
    }

    public void updateData(List<Video> newVideoList) {
        for (Video video : newVideoList) {
            video.setExpanded(false);
        }
        this.videoList.clear();
        if (newVideoList != null) {
            this.videoList.addAll(newVideoList);
        }
        notifyDataSetChanged();
    }

    private String formatUploadTime(String rawUploadTime) {
        if (rawUploadTime == null || rawUploadTime.isEmpty()) {
            return "정보 없음";
        }
        if (rawUploadTime.contains("T")) {
            return rawUploadTime.substring(0, rawUploadTime.indexOf("T"));
        }
        return rawUploadTime;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llVideoItemContainer, llVideoSummaryInfo, llVideoDetailInfo;
        ImageView ivVideoThumbnail;
        TextView tvVideoTitle, tvVideoViewCount;
        TextView tvVideoFullDescription, tvVideoUploadTime;
        LinearLayout llVideoTagsContainer;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llVideoItemContainer = itemView.findViewById(R.id.llVideoItemContainer);
            llVideoSummaryInfo = itemView.findViewById(R.id.llVideoSummaryInfo);
            llVideoDetailInfo = itemView.findViewById(R.id.llVideoDetailInfo);
            ivVideoThumbnail = itemView.findViewById(R.id.ivVideoThumbnail);
            tvVideoTitle = itemView.findViewById(R.id.tvVideoTitle);
            tvVideoViewCount = itemView.findViewById(R.id.tvVideoViewCount);
            tvVideoFullDescription = itemView.findViewById(R.id.tvVideoFullDescription);
            tvVideoUploadTime = itemView.findViewById(R.id.tvVideoUploadTime);
            llVideoTagsContainer = itemView.findViewById(R.id.llVideoTagsContainer);
        }
    }
}