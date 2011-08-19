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

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A preference that provides a dialog with a slider for idle time.
 *
 * The slider produces a value in seconds from 60 (1 minute) to 21600
 * (6 hours). The values range on an approximately exponential scale.
 */
public class IdlePreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
	private static int MIN = 60;
	private static int MAX = 21600;

	/**
	 * The current idle timeout displayed on the slider. Will not be persisted
	 * until the dialog is closed.
	 */
	private int mValue;
	/**
	 * The view in which the value is displayed.
	 */
	private TextView mValueText;

	public IdlePreference(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder)
	{
		Context context = getContext();
		ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		mValue = getPersistedInt(3600);

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(params);

		mValueText = new TextView(context);
		mValueText.setGravity(Gravity.RIGHT);
		mValueText.setPadding(20, 0, 20, 0);
		layout.addView(mValueText);

		SeekBar seekBar = new SeekBar(context);
		seekBar.setPadding(20, 0, 20, 20);
		seekBar.setLayoutParams(params);
		seekBar.setMax(1000);
		seekBar.setProgress((int)(Math.pow((float)(mValue - MIN) / (MAX - MIN), 0.25f) * 1000));
		seekBar.setOnSeekBarChangeListener(this);
		layout.addView(seekBar);

		updateText();

		builder.setView(layout);
	}

	/**
	 * Update the text view with the current value.
	 */
	private void updateText()
	{
		Resources res = getContext().getResources();
		int value = mValue;
		String text;
		if (value >= 3570) {
			int hours = (int)Math.round(value / 3600f);
			text = res.getQuantityString(R.plurals.hours, hours, hours);
		} else {
			int minutes = (int)Math.round(value / 60f);
			text = res.getQuantityString(R.plurals.minutes, minutes, minutes);
		}
		mValueText.setText(text);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult)
	{
		if (positiveResult && shouldPersist())
			persistInt(mValue);
	}

	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		// Approximate an exponential curve with x^4. Produces a value from 60-10860.
		if (fromUser) {
			float value = seekBar.getProgress() / 1000.0f;
			value *= value;
			value *= value;
			mValue = (int)(value * (MAX - MIN)) + MIN;
			updateText();
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar)
	{		
	}

	public void onStopTrackingTouch(SeekBar seekBar)
	{
	}
}