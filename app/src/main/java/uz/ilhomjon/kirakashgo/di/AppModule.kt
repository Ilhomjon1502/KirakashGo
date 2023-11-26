package uz.ilhomjon.kirakashgo.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.ilhomjon.kirakashgo.data.remote.ApiService
import uz.ilhomjon.kirakashgo.domain.repository.DriverRepository
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverProfileViewModel
import uz.ilhomjon.kirakashgo.utils.Const.BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String = BASE_URL


    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideDriverRepository(
        apiService: ApiService, @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DriverRepository {
        return DriverRepository(
            apiService = apiService
        )
    }

    @Provides
    @Singleton
    fun provideDriverProfileViewModel(repository: DriverRepository): DriverProfileViewModel {
        return DriverProfileViewModel(repository)
    }
}
