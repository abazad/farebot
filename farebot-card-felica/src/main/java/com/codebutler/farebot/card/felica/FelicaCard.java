/*
 * FelicaCard.java
 *
 * This file is part of FareBot.
 * Learn more at: https://codebutler.github.io/farebot/
 *
 * Copyright (C) 2011-2012, 2014, 2016 Eric Butler <eric@codebutler.com>
 * Copyright (C) 2013 Chris Norden <thisiscnn@gmail.com>
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

package com.codebutler.farebot.card.felica;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codebutler.farebot.card.felica.ui.FelicaCardRawDataFragment;
import com.codebutler.farebot.core.ByteArray;
import com.codebutler.farebot.card.Card;
import com.codebutler.farebot.card.CardRawDataFragmentClass;
import com.codebutler.farebot.card.CardType;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import net.kazzz.felica.lib.FeliCaLib;

import java.util.Date;
import java.util.List;

@CardRawDataFragmentClass(FelicaCardRawDataFragment.class)
@AutoValue
public abstract class FelicaCard extends Card {

    @NonNull
    public static FelicaCard create(
            @NonNull ByteArray tagId,
            @NonNull Date scannedAt,
            @NonNull FeliCaLib.IDm idm,
            @NonNull FeliCaLib.PMm pmm,
            @NonNull List<FelicaSystem> systems) {
        return new AutoValue_FelicaCard(
                tagId,
                scannedAt,
                idm,
                pmm,
                systems);
    }

    @NonNull
    public CardType getCardType() {
        return CardType.FeliCa;
    }

    @NonNull
    public static TypeAdapter<FelicaCard> typeAdapter(@NonNull Gson gson) {
        return new AutoValue_FelicaCard.GsonTypeAdapter(gson);
    }

    @NonNull
    public abstract FeliCaLib.IDm getIDm();

    @NonNull
    public abstract FeliCaLib.PMm getPMm();

    @NonNull
    public abstract List<FelicaSystem> getSystems();

    @Nullable
    public FelicaSystem getSystem(int systemCode) {
        for (FelicaSystem system : getSystems()) {
            if (system.getCode() == systemCode) {
                return system;
            }
        }
        return null;
    }
}
