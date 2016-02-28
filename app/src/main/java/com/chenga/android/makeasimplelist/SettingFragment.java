package com.chenga.android.makeasimplelist;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingFragment extends Fragment {

    private Widget mInfo;

    private EditText mTitleField;
    private Button mTextSmallButton;
    private Button mTextMediumButton;
    private Button mTextBigButton;
    private Button mTextBlackButton;
    private Button mTextRedButton;
    private Button mTextBlueButton;
    private Button mBGWhiteButton;
    private Button mBGRedButton;
    private Button mBGOrangeButton;
    private Button mBGYellowButton;
    private Button mBGGreenButton;
    private Button mBGBlueButton;
    private TextView mSampleTextField;

    private String title;
    private int textColor, textSize, bgColor, bgSColor;

    private static final int SMALL_FONT_SIZE = 10;
    private static final int MEDIUM_FONT_SIZE = 20;
    private static final int LARGE_FONT_SIZE = 30;

    private static final String ARG_SETTINGS = "settings";

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            title = savedInstanceState.getString("title");
            textSize = savedInstanceState.getInt("textsize", 0);
            textColor = savedInstanceState.getInt("textcolor", 0);
            bgColor = savedInstanceState.getInt("bgcolor", 0);
            bgSColor = savedInstanceState.getInt("bgscolor", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.get_info, container, false);

        mInfo = new Widget();

        mTitleField = (EditText) v.findViewById(R.id.set_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mInfo.setTitle(s.toString());
                title = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSampleTextField = (TextView) v.findViewById(R.id.info_sample_text);

        mTextSmallButton = (Button) v.findViewById(R.id.set_text_small);
        mTextSmallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setTextSize(SMALL_FONT_SIZE);
                mSampleTextField.setTextSize(TypedValue.COMPLEX_UNIT_SP, SMALL_FONT_SIZE);
                textSize = SMALL_FONT_SIZE;
            }
        });

        mTextMediumButton = (Button) v.findViewById(R.id.set_text_medium);
        mTextMediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setTextSize(MEDIUM_FONT_SIZE);
                mSampleTextField.setTextSize(TypedValue.COMPLEX_UNIT_SP, MEDIUM_FONT_SIZE);
                textSize = MEDIUM_FONT_SIZE;
            }
        });

        mTextBigButton = (Button) v.findViewById(R.id.set_text_large);
        mTextBigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setTextSize(LARGE_FONT_SIZE);
                mSampleTextField.setTextSize(TypedValue.COMPLEX_UNIT_SP, LARGE_FONT_SIZE);
                textSize = LARGE_FONT_SIZE;
            }
        });

        mTextBlackButton = (Button) v.findViewById(R.id.set_text_black);
        mTextBlackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                mSampleTextField.setTextColor(ContextCompat.getColor(getActivity(),
                        R.color.colorBlack));

                textColor = mInfo.getTextColor();
            }
        });

        mTextRedButton = (Button) v.findViewById(R.id.set_text_red);
        mTextRedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setTextColor(ContextCompat.getColor(getActivity(),
                        R.color.colorDarkRed));
                mSampleTextField.setTextColor(ContextCompat.getColor(getActivity(),
                    R.color.colorDarkRed));

                textColor = mInfo.getTextColor();
            }
        });

        mTextBlueButton = (Button) v.findViewById(R.id.set_text_blue);
        mTextBlueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setTextColor(ContextCompat.getColor(getActivity(),
                        R.color.colorDarkBlue));
                mSampleTextField.setTextColor(ContextCompat.getColor(getActivity(),
                    R.color.colorDarkBlue));

                textColor = mInfo.getTextColor();
            }
        });

        mBGWhiteButton = (Button) v.findViewById(R.id.set_bg_white);
        mBGWhiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setBGColor(R.drawable.rectangle_white_bg);
                mSampleTextField.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.colorWhite));

                bgColor = mInfo.getBGColor();
                bgSColor = ContextCompat.getColor(getActivity(),
                        R.color.colorWhite);
            }
        });

        mBGRedButton = (Button) v.findViewById(R.id.set_bg_red);
        mBGRedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setBGColor(R.drawable.rectangle_red_bg);
                mSampleTextField.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.colorLightRed));

                bgColor = mInfo.getBGColor();
                bgSColor = ContextCompat.getColor(getActivity(),
                        R.color.colorLightRed);
            }
        });


        mBGOrangeButton = (Button) v.findViewById(R.id.set_bg_orange);
        mBGOrangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setBGColor(R.drawable.rectangle_orange_bg);
                mSampleTextField.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.colorLightOrange));

                bgColor = mInfo.getBGColor();
                bgSColor = ContextCompat.getColor(getActivity(),
                        R.color.colorLightOrange);
            }
        });

        mBGYellowButton = (Button) v.findViewById(R.id.set_bg_yellow);
        mBGYellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setBGColor(R.drawable.rectangle_yellow_bg);
                mSampleTextField.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.colorLightYellow));

                bgColor = mInfo.getBGColor();
                bgSColor = ContextCompat.getColor(getActivity(),
                        R.color.colorLightYellow);
            }
        });

        mBGGreenButton = (Button) v.findViewById(R.id.set_bg_green);
        mBGGreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setBGColor(R.drawable.rectangle_green_bg);
                mSampleTextField.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.colorLightGreen));

                bgColor = mInfo.getBGColor();
                bgSColor = ContextCompat.getColor(getActivity(),
                        R.color.colorLightGreen);
            }
        });

        mBGBlueButton = (Button) v.findViewById(R.id.set_bg_blue);
        mBGBlueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo.setBGColor(R.drawable.rectangle_blue_bg);
                mSampleTextField.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.colorLightBlue));

                bgColor = mInfo.getBGColor();
                bgSColor = ContextCompat.getColor(getActivity(),
                        R.color.colorLightBlue);
            }
        });

        if(title != null) {
            mTitleField.setText(title);
            mInfo.setTitle(title);
        }

        if (textSize != 0) {
            mSampleTextField.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            mInfo.setTextSize(textSize);
        }

        if (textColor != 0) {
            mSampleTextField.setTextColor(textColor);
            mInfo.setTextColor(textColor);
        }

        if (bgColor != 0) {
            mSampleTextField.setBackgroundColor(bgSColor);
            mInfo.setBGColor(bgColor);
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("title", title);
        savedInstanceState.putInt("textsize", textSize);
        savedInstanceState.putInt("textcolor", textColor);
        savedInstanceState.putInt("bgcolor", bgColor);
        savedInstanceState.putInt("bgscolor", bgSColor);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_done, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_done:
                Intent intent = new Intent(WidgetProvider.ACTION_SETTINGS_CHANGED);
                intent.putExtra(ARG_SETTINGS, mInfo);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        getActivity().getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                AppWidgetManager.INVALID_APPWIDGET_ID
                ));
                getActivity().getApplicationContext().sendBroadcast(intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
