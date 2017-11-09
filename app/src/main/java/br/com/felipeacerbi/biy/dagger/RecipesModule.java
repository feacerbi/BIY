package br.com.felipeacerbi.biy.dagger;

import javax.inject.Singleton;

import br.com.felipeacerbi.biy.retrofit.RecipesApiService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
class RecipesModule {

    @Provides
    @Singleton
    RecipesApiService getRecipesApiService(Retrofit retrofit) {
        return retrofit.create(RecipesApiService.class);
    }

}
