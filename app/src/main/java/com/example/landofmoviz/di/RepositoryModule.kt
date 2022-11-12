package com.example.landofmoviz.di


import com.example.landofmoviz.data.repository.MovieRepositoryImpl
import com.example.landofmoviz.data.repository.PersonRepositoryImpl
import com.example.landofmoviz.data.repository.TvRepositoryImpl
import com.example.landofmoviz.domain.repository.MovieRepository
import com.example.landofmoviz.domain.repository.PersonRepository
import com.example.landofmoviz.domain.repository.TvRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMovieRepository(repository: MovieRepositoryImpl): MovieRepository

    @Binds
    abstract fun bindTvRepository(repository: TvRepositoryImpl): TvRepository

    @Binds
    abstract fun bindPersonRepository(repository: PersonRepositoryImpl): PersonRepository
}