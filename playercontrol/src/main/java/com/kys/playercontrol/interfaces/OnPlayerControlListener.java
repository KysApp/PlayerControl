/*****************************************************************************
 * OnPlayerControlListener.java
 *****************************************************************************
 * Copyright © 2012 VLC authors and VideoLAN
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package com.kys.playercontrol.interfaces;

import android.widget.GridView;
import android.widget.ListView;

public interface OnPlayerControlListener {
    public abstract void onPlayPause();

    public abstract void onSeekTo(int position);

    public abstract void onState(boolean isPlaying);

    public abstract void onVideoLength();

    public abstract void onCurrentPosition();

    public abstract void onTouchCurrentPosition();

    public abstract boolean canShowProgress();

    public abstract void setDefinition(ListView mListView);

    public abstract void setChannelList(ListView mListView);

    public abstract void setDlna(ListView mListView);

    public abstract void setSeries(GridView mGridView);

    public abstract void getFavorite();

    public abstract void setOnBackPressed();

    public abstract void isSmallVolBri();

    public abstract void isFullVolBri();

    public abstract void onRefreshDlna();
}
