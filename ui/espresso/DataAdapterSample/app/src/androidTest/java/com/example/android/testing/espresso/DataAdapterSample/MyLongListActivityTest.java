package com.example.android.testing.espresso.DataAdapterSample;
/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

/**
 * Tests to verify that the behavior of {@link LongListActivity} is correct.
 * <p>
 * Note that in order to scroll the list you shouldn't use {@link ViewActions#scrollTo()} as
 * {@link Espresso#onData(org.hamcrest.Matcher)} handles scrolling.</p>
 *
 * @see #onRow(String)
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyLongListActivityTest {

    private static final String TEXT_ITEM_30 = "item: 30";

    private static final String TEXT_ITEM_30_SELECTED = "30";

    private static final String TEXT_ITEM_60 = "item: 60";

    // Match the last item by matching its text.
    private static final String LAST_ITEM_ID = "item: 99";

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test. This is a
     * replacement for {@link androidx.test.rule.ActivityTestRule}.
     */
    @Rule
    public ActivityScenarioRule<LongListActivity> rule = new ActivityScenarioRule<>(
        LongListActivity.class);

    /**
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    public void lastItem_NotDisplayed() {
        // Last item should not exist if the list wasn't scrolled down.
        Espresso.onView(ViewMatchers.withText(LAST_ITEM_ID)).check(ViewAssertions.doesNotExist());
    }

    /**
     * Check that the item is created. onData() takes care of scrolling.
     */
    @Test
    public void list_Scrolls() {
        onRow(LAST_ITEM_ID).check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));
    }

    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void row_Click() {
        // Click on one of the rows.
        onRow(TEXT_ITEM_30).onChildView(ViewMatchers.withId(R.id.rowContentTextView)).perform(ViewActions.click());

        // Check that the activity detected the click on the first column.
        Espresso.onView(ViewMatchers.withId(R.id.selection_row_value))
                .check(ViewAssertions.matches(ViewMatchers.withText(TEXT_ITEM_30_SELECTED)));
    }

    /**
     * Checks that a toggle button is checked after clicking on it.
     */
    @Test
    public void toggle_Click() {
        // Click on a toggle button.
        onRow(TEXT_ITEM_30).onChildView(ViewMatchers.withId(R.id.rowToggleButton)).perform(ViewActions.click());

        // Check that the toggle button is checked.
        onRow(TEXT_ITEM_30).onChildView(ViewMatchers.withId(R.id.rowToggleButton)).check(ViewAssertions.matches(ViewMatchers.isChecked()));
    }

    /**
     * Make sure that clicking on the toggle button doesn't trigger a click on the row.
     */
    @Test
    public void toggle_ClickDoesntPropagate() {
        // Click on one of the rows.
        onRow(TEXT_ITEM_30).onChildView(ViewMatchers.withId(R.id.rowContentTextView)).perform(ViewActions.click());

        // Click on the toggle button, in a different row.
        onRow(TEXT_ITEM_60).onChildView(ViewMatchers.withId(R.id.rowToggleButton)).perform(ViewActions.click());

        // Check that the activity didn't detect the click on the first column.
        Espresso.onView(ViewMatchers.withId(R.id.selection_row_value))
                .check(ViewAssertions.matches(ViewMatchers.withText(TEXT_ITEM_30_SELECTED)));
    }

    /**
     * Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific row.
     * <p>
     * Note: A custom matcher can be used to match the content and have more readable code.
     * See the Custom Matcher Sample.
     * </p>
     *
     * @param str the content of the field
     * @return a {@link DataInteraction} referencing the row
     */
    private static DataInteraction onRow(String str) {
        return Espresso.onData(Matchers.hasEntry(Matchers.equalTo(LongListActivity.ROW_TEXT), Matchers.is(str)));
    }
}