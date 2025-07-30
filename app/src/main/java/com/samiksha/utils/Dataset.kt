package com.samiksha.utils

import com.samiksha.R

object Dataset {
    fun getMenuImage(weightage: String?): Int {
        if (weightage == "1"){
            return R.drawable.feedback_1
        }else if (weightage == "2"){
            return R.drawable.feedback_2
        }else if (weightage == "3"){
            return R.drawable.feedback_3
        }else if (weightage == "4"){
            return R.drawable.feedback_4
        }else if (weightage == "5"){
            return R.drawable.feedback_5
        }else{
            return R.drawable.feedback_1
        }

    }
}