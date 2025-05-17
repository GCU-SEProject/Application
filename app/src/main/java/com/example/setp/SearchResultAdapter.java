package com.example.setp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private Context context;
    private List<DataModel> dataList;

    public SearchResultAdapter(Context context, List<DataModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (dataList != null && !dataList.isEmpty() && position < dataList.size()) {
            DataModel item = dataList.get(position);
            holder.tvItemTitle.setText(item.getTitle());
            holder.tvItemDescription.setText(item.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void updateData(List<DataModel> newDataList) {
        this.dataList.clear();
        if (newDataList != null) {
            this.dataList.addAll(newDataList);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemTitle;
        TextView tvItemDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tvItemTitle);
            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
        }
    }
}
