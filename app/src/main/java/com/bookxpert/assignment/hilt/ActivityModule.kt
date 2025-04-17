package com.bookxpert.assignment.hilt

import com.bookxpert.assignment.base.BaseActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {
    /**
     * This method provides BaseActivity instance to Hilt DI.
     * Ensure that the activity is indeed a BaseActivity to avoid ClassCastException.
     */
    @Provides
    fun provideBaseActivity(activity: BaseActivity): BaseActivity {
        return activity
    }
}