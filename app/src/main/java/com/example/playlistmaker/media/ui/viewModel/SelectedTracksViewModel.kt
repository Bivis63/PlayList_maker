package com.example.playlistmaker.media.ui.viewModel


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.HistoryInteractor
import com.example.playlistmaker.media.ui.History.HistoryState
import kotlinx.coroutines.launch

class SelectedTracksViewModel(
    private val context: Context,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<HistoryState>()
    fun observeState(): LiveData<HistoryState> = stateLiveData

    fun getFavoriteTrack() {
        viewModelScope.launch {
            historyInteractor
                .getTracks()
                .collect { tracks ->
                    if (tracks.isEmpty()) {
                        renderState(HistoryState.Empty)

                    } else {
                        renderState(HistoryState.Content(tracks))
                    }
                }
        }
    }


    fun renderState(state: HistoryState) {
        stateLiveData.postValue(state)
    }
}