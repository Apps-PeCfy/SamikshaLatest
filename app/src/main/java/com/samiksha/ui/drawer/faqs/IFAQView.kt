package com.samiksha.ui.drawer.faqs

import com.samiksha.base.IBaseView
import com.samiksha.ui.drawer.faqs.POJO.FAQResponsePOJO

interface IFAQView: IBaseView {
    fun onFaqSUccess(faqResponsePOJO:FAQResponsePOJO?)

}