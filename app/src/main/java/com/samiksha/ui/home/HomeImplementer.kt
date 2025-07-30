package com.samiksha.ui.home

import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO
import com.samiksha.ui.home.pojo.CategoriesResponsePOJO
import com.samiksha.ui.home.pojo.TestimonialsResponsePOJO
import com.samiksha.ui.register.pojo.StateResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeImplementer(var view: IHomeView) :
    IHomePresenter  {
    override fun allcategories(token: String?) {
       // view.showWait()
        val call = ApiClient.client.getMentalSkillCategories("Bearer $token")
        call!!.enqueue(object : Callback<AllCategoriesResponsePOJO?> {
            override fun onResponse(
                call: Call<AllCategoriesResponsePOJO?>,
                response: Response<AllCategoriesResponsePOJO?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.categoriesSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.removeWait()
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

    override fun allskills(token: String?) {
       view.showWait()
        val call = ApiClient.client.getMentalSkills("Bearer $token","All")
        call!!.enqueue(object : Callback<CategoriesResponsePOJO?> {
            override fun onResponse(
                call: Call<CategoriesResponsePOJO?>,
                response: Response<CategoriesResponsePOJO?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.allSkillSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.removeWait()
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<CategoriesResponsePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })

    }

    override fun state(country_id: String?) {
        val call = ApiClient.client.getState(country_id)
        call!!.enqueue(object : Callback<StateResponsePOJO?> {
            override fun onResponse(
                call: Call<StateResponsePOJO?>,
                response: Response<StateResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    view.onStateSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<StateResponsePOJO?>,
                t: Throwable
            ) {
                view.onFailure(t.message)
            }
        })
    }

    override fun yogaSelected(token: String?, yoga: String?) {
        view.showWait()
        val call = ApiClient.client.yogaGetMentalSkillsSelected("Bearer $token",yoga)
        call!!.enqueue(object : Callback<CategoriesResponsePOJO?> {
            override fun onResponse(
                call: Call<CategoriesResponsePOJO?>,
                response: Response<CategoriesResponsePOJO?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.yogaSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.removeWait()
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<CategoriesResponsePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })

    }

    override fun selectedCategories(token: String?, value: String?) {

        view.showWait()
        val call = ApiClient.client.getMentalSkillsSelected("Bearer $token",value.toString())
        call!!.enqueue(object : Callback<CategoriesResponsePOJO?> {
            override fun onResponse(
                call: Call<CategoriesResponsePOJO?>,
                response: Response<CategoriesResponsePOJO?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.selectedCategoriesSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.removeWait()
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<CategoriesResponsePOJO?>,
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

                if (response.code() == 200) {
                    view.removeWait()
                    view.onSkillDetails(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.removeWait()
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

    override fun addToFavorites(token: String?, context_type: String?, context_id: String?) {
      //  view.showWait()
        val call = ApiClient.client.addToFavorite("Bearer $token",context_type, context_id)
        call!!.enqueue(object : Callback<SkillDetailsResponsePOJO?> {
            override fun onResponse(
                call: Call<SkillDetailsResponsePOJO?>,
                response: Response<SkillDetailsResponsePOJO?>
            ) {

                if (response.code() == 200) {
                  //  view.removeWait()
                    view.addFavoriteSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                   // view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SkillDetailsResponsePOJO?>,
                t: Throwable
            ) {
              //  view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun deleteFromFavorites(token: String?, favorite_id: String?) {
     //   view.showWait()
        val call = ApiClient.client.deleteFromFavorites("Bearer $token",favorite_id)
        call!!.enqueue(object : Callback<SkillDetailsResponsePOJO?> {
            override fun onResponse(
                call: Call<SkillDetailsResponsePOJO?>,
                response: Response<SkillDetailsResponsePOJO?>
            ) {

                if (response.code() == 200) {
                   // view.removeWait()
                    view.deleteFromFavoriteSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                 //   view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SkillDetailsResponsePOJO?>,
                t: Throwable
            ) {
              //  view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun allTestimonials(token: String?) {
        val call = ApiClient.client.getTestimonials("Bearer $token")
        call!!.enqueue(object : Callback<TestimonialsResponsePOJO?> {
            override fun onResponse(
                call: Call<TestimonialsResponsePOJO?>,
                response: Response<TestimonialsResponsePOJO?>
            ) {

                if (response.code() == 200) {
                    view.testimonialsSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<TestimonialsResponsePOJO?>,
                t: Throwable
            ) {
                view.onFailure(t.message)
            }
        })

    }


}