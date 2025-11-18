package com.rustan.story.di

import com.rustan.story.StoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val storyFeatureModule = module {
    viewModel { StoryViewModel(get()) }
}