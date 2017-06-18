package io.psych.io2017.downloadablefonts;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.fonts.FontRequest;
import android.os.Bundle;
import android.os.Handler;
import android.provider.FontsContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import io.psych.io2017.R;

public class DownloadableFonts extends Activity {

    private static final String TAG = "DownloadableFonts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadable_fonts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        loadFontFromGoogleFonts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // This won't work yet because it requires the Google Font Provider to be installed on the
    // device, and the Android O emulator does not appear to have it installed.
    private void loadFontFromGoogleFonts() {
        final TextView downloadableFontTextView = findViewById(R.id.textview_runtime_font);

        FontRequest request = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Faster One"
        );

        FontsContract.FontRequestCallback callback = new FontsContract.FontRequestCallback() {
            @Override
            public void onTypefaceRetrieved(Typeface typeface) {
                downloadableFontTextView.setTypeface(typeface);
            }

            @Override
            public void onTypefaceRequestFailed(int reason) {
                Log.d(TAG, "Failed to download 'Faster One' font. Reason: " + reason);
            }
        };

        FontsContract.requestFont(this, request, callback, new Handler());
    }
}
