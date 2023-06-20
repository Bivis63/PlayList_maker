package com.example.playlistmaker.settings.ui.viewmodel

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.playlistmaker.settings.data.SharedPreferencesThemeRepository
import com.example.playlistmaker.settings.domain.usecase.ThemeUseCase
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.model.EmailData
import com.example.playlistmaker.util.THEM_SWITCHER

class SettingViewModel(
    private val sharingInteractor: SharingInteractorImpl,
    private val themeUseCase: ThemeUseCase
) : ViewModel() {
    private val _isDarkThemeEnable = MutableLiveData<Boolean>()
    val isDarkThemeEnable: LiveData<Boolean> = _isDarkThemeEnable

    init {
        _isDarkThemeEnable.value = themeUseCase.isDarkThemeEnable()
    }

    fun setTheme(isDark: Boolean) {
        themeUseCase.setTheme(isDark)
        _isDarkThemeEnable.value = isDark
    }

    fun shareApp(url: String) {
        sharingInteractor.shareApp(url)
    }

    fun openTerms(url: String) {
        sharingInteractor.openTerms(url)
    }

    fun openSupport(emailData: EmailData) {
        sharingInteractor.openSupport(emailData)
    }

    companion object {
        fun getSettingViewModelFactory(

        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val application = checkNotNull(extras[APPLICATION_KEY])
                    val externalNavigator = ExternalNavigatorImpl(application)
                    val sharingInteractor = SharingInteractorImpl(externalNavigator)
                    val sharedPref = application.getSharedPreferences(THEM_SWITCHER, MODE_PRIVATE)
                    val themeRepository = SharedPreferencesThemeRepository(sharedPref)
                    val themeUseCase = ThemeUseCase(themeRepository)
                    return SettingViewModel(sharingInteractor, themeUseCase) as T
                }
            }
    }
}