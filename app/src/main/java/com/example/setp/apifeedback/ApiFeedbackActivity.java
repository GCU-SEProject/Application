package com.example.setp.apifeedback;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.setp.R;

public class ApiFeedbackActivity extends AppCompatActivity {

    private TextView tvNaverRequestCount, tvNaverRemainingQuota;
    private ProgressBar pbNaverApiUsage;
    private TextView tvYoutubeRequestCount, tvYoutubeRemainingQuota;
    private ProgressBar pbYoutubeApiUsage;
    private TextView tvGameRequestCount, tvGameRemainingQuota;
    private ProgressBar pbGameApiUsage;
    private TextView tvNewsRequestCount, tvNewsRemainingQuota;
    private ProgressBar pbNewsApiUsage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_feedback);
        setTitle(getString(R.string.title_api_feedback));

        tvNaverRequestCount = findViewById(R.id.tvNaverRequestCount);
        tvNaverRemainingQuota = findViewById(R.id.tvNaverRemainingQuota);
        pbNaverApiUsage = findViewById(R.id.pbNaverApiUsage);
        tvYoutubeRequestCount = findViewById(R.id.tvYoutubeRequestCount);
        tvYoutubeRemainingQuota = findViewById(R.id.tvYoutubeRemainingQuota);
        pbYoutubeApiUsage = findViewById(R.id.pbYoutubeApiUsage);
        tvGameRequestCount = findViewById(R.id.tvGameRequestCount);
        tvGameRemainingQuota = findViewById(R.id.tvGameRemainingQuota);
        pbGameApiUsage = findViewById(R.id.pbGameApiUsage);
        tvNewsRequestCount = findViewById(R.id.tvNewsRequestCount);
        tvNewsRemainingQuota = findViewById(R.id.tvNewsRemainingQuota);
        pbNewsApiUsage = findViewById(R.id.pbNewsApiUsage);

        loadApiUsageData();
    }

    private void loadApiUsageData() {
        updateApiStatus(tvNaverRequestCount, tvNaverRemainingQuota, pbNaverApiUsage, 0, 0, 0);
        updateApiStatus(tvYoutubeRequestCount, tvYoutubeRemainingQuota, pbYoutubeApiUsage, 0, 0, 0);
        updateApiStatus(tvGameRequestCount, tvGameRemainingQuota, pbGameApiUsage, 0, 0, 0);
        updateApiStatus(tvNewsRequestCount, tvNewsRemainingQuota, pbNewsApiUsage, 0, 0, 0);
    }

    private void updateApiStatus(TextView tvRequest, TextView tvRemaining, ProgressBar pbUsage,
                                 int requestCount, int remainingQuota, int progressPercent) {
        tvRequest.setText(String.format(getString(R.string.request_count), requestCount));
        tvRemaining.setText(String.format(getString(R.string.remaining_quota), remainingQuota));
        pbUsage.setProgress(progressPercent);
    }
}