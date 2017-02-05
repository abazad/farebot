/*
 * UltralightCardRawDataFragment.java
 *
 * This file is part of FareBot.
 * Learn more at: https://codebutler.github.io/farebot/
 *
 * Copyright (C) 2016 Eric Butler <eric@codebutler.com>
 * Copyright (C) 2016 Michael Farrell <micolous+git@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.codebutler.farebot.card.ultralight.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.codebutler.farebot.card.CardUiDependencies;
import com.codebutler.farebot.card.serialize.CardSerializer;
import com.codebutler.farebot.card.ultralight.R;
import com.codebutler.farebot.card.ultralight.UltralightPage;
import com.codebutler.farebot.card.ultralight.raw.RawUltralightCard;
import com.codebutler.farebot.core.ByteUtils;
import com.codebutler.farebot.core.Constants;
import com.codebutler.farebot.core.ui.ExpandableListFragment;

/**
 * Shows raw data of the Mifare Ultralight / Ultralight C
 */
public class UltralightCardRawDataFragment extends ExpandableListFragment {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        CardSerializer cardSerializer = ((CardUiDependencies) getActivity().getApplication()).getCardSerializer();
        String serializedCard = getArguments().getString(Constants.EXTRA_RAW_CARD);
        RawUltralightCard rawCard = (RawUltralightCard) cardSerializer.deserialize(serializedCard);

        setListAdapter(new UltralightRawDataAdapter(getActivity(), rawCard));
    }

    private static class UltralightRawDataAdapter extends BaseExpandableListAdapter {
        private Activity mActivity;
        private RawUltralightCard mRawCard;

        private UltralightRawDataAdapter(Activity activity, RawUltralightCard rawCard) {
            mActivity = activity;
            mRawCard = rawCard;
        }

        @Override
        public int getGroupCount() {
            return mRawCard.pages().size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public UltralightPage getGroup(int groupPosition) {
            return mRawCard.pages().get(groupPosition);
        }

        @Override
        public UltralightPage getChild(int groupPosition, int childPosition) {
            return mRawCard.pages().get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition + childPosition + 100000;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = mActivity.getLayoutInflater()
                        .inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            }

            UltralightPage sector = getGroup(groupPosition);
            String sectorIndexString = Integer.toHexString(sector.getIndex());

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(mActivity.getString(R.string.page_title_format, sectorIndexString));

            return view;
        }

        @Override
        public View getChildView(
                int groupPosition,
                int childPosition,
                boolean isLastChild,
                View convertView,
                ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = mActivity.getLayoutInflater()
                        .inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
            }

            TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
            TextView textView2 = (TextView) view.findViewById(android.R.id.text2);

            UltralightPage page = getChild(groupPosition, childPosition);
            byte[] block = page.getData().bytes();
            textView1.setText(mActivity.getString(R.string.block_title_format, String.valueOf(page.getIndex())));
            textView2.setText(ByteUtils.getHexString(block));

            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
