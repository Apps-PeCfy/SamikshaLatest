package com.samiksha.ui.home.dealingWithDistraction

import com.samiksha.base.IBaseView
import com.samiksha.ui.home.dealingWithDistraction.pojo.OrderModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel
import com.samiksha.ui.register.pojo.StateResponsePOJO

interface IDealingDistractionView:IBaseView {

    fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?)

    fun onSubscriptionList(subscriptionModel: SubscriptionModel?)

    fun createOrderSuccess(model: OrderModel?)

    fun updateSubscription(subscriptionModel: SubscriptionModel?)

    fun onStateSuccess(stateResponcePOJO: StateResponsePOJO?)


}