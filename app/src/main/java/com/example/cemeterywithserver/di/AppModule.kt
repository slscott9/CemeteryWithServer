package com.example.cemeterywithserver.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.cemeterywithserver.data.local.CemeteryDao
import com.example.cemeterywithserver.data.local.CemeteryDatabase
import com.example.cemeterywithserver.data.remote.BasicAuthInterceptor
import com.example.cemeterywithserver.data.remote.CemeteryApi
import com.example.cemeterywithserver.other.Constants.BASE_URL
import com.example.cemeterywithserver.other.Constants.DATABASE_NAME
import com.example.cemeterywithserver.other.Constants.ENCRYPTED_SHARED_PREF_NAME
import com.example.cemeterywithserver.repositories.CemRepoImpl
import com.example.cemeterywithserver.repositories.CemeteryRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCemeteryDao(db: CemeteryDatabase) = db.cemeteryDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context)
            = Room.databaseBuilder(appContext, CemeteryDatabase::class.java, DATABASE_NAME).fallbackToDestructiveMigration().build()


    @Singleton
    @Provides
    fun provideRepository(dao: CemeteryDao, cemeteryApi: CemeteryApi, context: Application) : CemeteryRepository =
        CemRepoImpl(dao, cemeteryApi, context)

    /*
       Why create a specific provides method for the interceptor?
       If we created the instance inside of provide note api method we would not be able to access and set the email and password members.
       So we need to make a provides method for the interceptor so we can inject an instance and access the fields
    */
    @Singleton
    @Provides
    fun provideBasicAuthInterceptor() = BasicAuthInterceptor()



    @Singleton
    @Provides
    fun provideCemeteryApi(basicAuthInterceptor: BasicAuthInterceptor): CemeteryApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(basicAuthInterceptor)
            .build()

//        val moshi = Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
//            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(CemeteryApi::class.java)
    }



    @Provides
    fun provideOkHttpclient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }


    @Singleton
    @Provides
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context) : SharedPreferences {
        //create master key for android key store
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM) //entrypts data
            .build()
        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_SHARED_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

}