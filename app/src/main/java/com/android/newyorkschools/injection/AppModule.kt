package  com.android.newyorkschools.injection

import com.android.newyorkschools.util.AppDispatchers
import com.android.newyorkschools.util.LocalDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDispatchers(): LocalDispatchers = AppDispatchers()

}