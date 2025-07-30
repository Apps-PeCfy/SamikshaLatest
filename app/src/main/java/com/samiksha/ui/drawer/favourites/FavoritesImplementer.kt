package com.samiksha.ui.drawer.favourites

import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesImplementer(var view: IFavoritesView) : IFavoritesPresenter  {

    override fun allCategories(token: String?) {
        view.showWait()
        val call = ApiClient.client.getMentalSkillCategories("Bearer $token")
        call!!.enqueue(object : Callback<AllCategoriesResponsePOJO?> {
            override fun onResponse(
                call: Call<AllCategoriesResponsePOJO?>,
                response: Response<AllCategoriesResponsePOJO?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.categoriesSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<AllCategoriesResponsePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun favoritesSkills(token: String?, category: String?, page: Int?) {
        view.showWait()
        val call = ApiClient.client.getFavoritesSkills("Bearer $token",category, page)
        call!!.enqueue(object : Callback<FavouritesModel?> {
            override fun onResponse(
                call: Call<FavouritesModel?>,
                response: Response<FavouritesModel?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.favoritesSkillSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<FavouritesModel?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun skillDetails(token :String?,skillID: String?) {

        view.showWait()
        val call = ApiClient.client.getMentalSkillDetails("Bearer $token",skillID)
        call!!.enqueue(object : Callback<SkillDetailsResponsePOJO?> {
            override fun onResponse(
                call: Call<SkillDetailsResponsePOJO?>,
                response: Response<SkillDetailsResponsePOJO?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.onSkillDetails(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SkillDetailsResponsePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })

    }

    override fun deleteFromFavorites(token: String?, favorite_id: String?) {
        view.showWait()
        val call = ApiClient.client.deleteFromFavorites("Bearer $token",favorite_id)
        call!!.enqueue(object : Callback<SkillDetailsResponsePOJO?> {
            override fun onResponse(
                call: Call<SkillDetailsResponsePOJO?>,
                response: Response<SkillDetailsResponsePOJO?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.deleteFromFavoriteSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SkillDetailsResponsePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }



}