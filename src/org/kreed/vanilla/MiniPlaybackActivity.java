/*
 * Copyright (C) 2010 Christopher Eby <kreed@kreed.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.kreed.vanilla;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Compact playback activity that displays itself like a dialog. That is, the
 * window is not fullscreen but only as large as it needs to be. Includes a
 * CoverView and control buttons.
 */
public class MiniPlaybackActivity extends PlaybackActivity implements View.OnClickListener {
	@Override
	public void onCreate(Bundle state)
	{
		super.onCreate(state);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mini_playback);

		mCoverView = (CoverView)findViewById(R.id.cover_view);
		mCoverView.setup(mLooper, this, CoverBitmap.STYLE_OVERLAPPING_BOX);

		View openButton = findViewById(R.id.open_button);
		openButton.setOnClickListener(this);
		View killButton = findViewById(R.id.kill_button);
		killButton.setOnClickListener(this);
		View previousButton = findViewById(R.id.previous);
		previousButton.setOnClickListener(this);
		mPlayPauseButton = (ControlButton)findViewById(R.id.play_pause);
		mPlayPauseButton.setOnClickListener(this);
		View nextButton = findViewById(R.id.next);
		nextButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId()) {
		case R.id.kill_button:
			ContextApplication.quit();
			break;
		case R.id.open_button:
			startActivity(new Intent(this, FullPlaybackActivity.class));
			finish();
			break;
		default:
			super.onClick(view);
		}
	}
}
