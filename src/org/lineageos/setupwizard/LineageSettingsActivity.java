/*
 * Copyright (C) 2016 The CyanogenMod Project
 *               2017-2020,2022 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.setupwizard;

import static org.lineageos.setupwizard.SetupWizardApp.DISABLE_NAV_KEYS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.setupcompat.util.WizardManagerHelper;

import lineageos.providers.LineageSettings;

public class LineageSettingsActivity extends BaseSetupWizardActivity {

    public static final String TAG = LineageSettingsActivity.class.getSimpleName();

    private SetupWizardApp mSetupWizardApp;

    private CheckBox mNavKeys;

    private final View.OnClickListener mNavKeysClickListener = view -> {
        boolean checked = !mNavKeys.isChecked();
        mNavKeys.setChecked(checked);
        mSetupWizardApp.getSettingsBundle().putBoolean(DISABLE_NAV_KEYS, checked);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSetupWizardApp = (SetupWizardApp) getApplication();
        setNextText(R.string.next);

        String os_name = getString(R.string.os_name);
        String privacyPolicy = getString(R.string.services_pp_explanation, os_name);
        String privacyPolicyUri = getString(R.string.services_privacy_policy_uri);
        String policySummary = getString(R.string.services_find_privacy_policy, privacyPolicyUri);
        String servicesFullDescription = getString(R.string.services_full_description,
                privacyPolicy, policySummary);
        getGlifLayout().setDescriptionText(servicesFullDescription);

        View navKeysRow = findViewById(R.id.nav_keys);
        navKeysRow.setOnClickListener(mNavKeysClickListener);
        mNavKeys = (CheckBox) findViewById(R.id.nav_keys_checkbox);
        mNavKeys.setChecked(LineageSettings.System.getIntForUser(getContentResolver(),
                LineageSettings.System.FORCE_SHOW_NAVBAR, 0, UserHandle.USER_CURRENT) != 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDisableNavkeysOption();
    }

    @Override
    protected void onNextPressed() {
        Intent intent = WizardManagerHelper.getNextIntent(getIntent(), Activity.RESULT_OK);
        nextAction(NEXT_REQUEST, intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.setup_lineage_settings;
    }

    @Override
    protected int getTitleResId() {
        return R.string.setup_services;
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_features;
    }

    private void updateDisableNavkeysOption() {
        final Bundle myPageBundle = mSetupWizardApp.getSettingsBundle();
        boolean enabled = LineageSettings.System.getIntForUser(getContentResolver(),
                LineageSettings.System.FORCE_SHOW_NAVBAR, 0, UserHandle.USER_CURRENT) != 0;
        boolean checked = myPageBundle.containsKey(DISABLE_NAV_KEYS) ?
                myPageBundle.getBoolean(DISABLE_NAV_KEYS) :
                enabled;
        mNavKeys.setChecked(checked);
        myPageBundle.putBoolean(DISABLE_NAV_KEYS, checked);
    }
}
