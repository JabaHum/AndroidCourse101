package com.koducation.androidcourse101.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.koducation.androidcourse101.SpotifyRadioApp
import com.koducation.androidcourse101.data.local.DatabaseProvider
import com.koducation.androidcourse101.data.local.entity.FavoriteRadioEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoriteFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteViewStateListLiveData = MutableLiveData<List<FavoriteRadioItemViewState>>()

    private val favoriteDataSource = (application as SpotifyRadioApp).getFavoriteDataSource()

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(favoriteDataSource
            .getFavoriteList()
            .map { mapToViewState(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                favoriteViewStateListLiveData.value = it
            })
    }

    fun getFavoriteRadiosLiveData(): LiveData<List<FavoriteRadioItemViewState>> =
        favoriteViewStateListLiveData

    fun removeFromFavorites(radioId: Int) {
        favoriteDataSource.removeFromFavorite(radioId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun mapToViewState(entityList: List<FavoriteRadioEntity>): List<FavoriteRadioItemViewState> {
        val viewStateList = arrayListOf<FavoriteRadioItemViewState>()
        entityList.forEach { viewStateList.add(FavoriteRadioItemViewState(it)) }
        return viewStateList
    }
}