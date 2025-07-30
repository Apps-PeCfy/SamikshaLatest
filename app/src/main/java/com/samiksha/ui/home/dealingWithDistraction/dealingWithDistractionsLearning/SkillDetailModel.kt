package com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning

import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO

class SkillDetailModel {
     var learningFirstQuestionModel : SkillDetailsResponsePOJO.DataItem ?= null
     var learningVideoModel : SkillDetailsResponsePOJO.DataItem ?= null
     var learningSecondQuestionModel : SkillDetailsResponsePOJO.DataItem ?= null
     var learningThirdQuestionModel : SkillDetailsResponsePOJO.DataItem ?= null
     var trainingAudioModel : SkillDetailsResponsePOJO.DataItem ?= null
     var trainingQuestionModel : SkillDetailsResponsePOJO.DataItem ?= null
     var trainingSecondQuestionModel : SkillDetailsResponsePOJO.DataItem ?= null
     var skillDetail : SkillDetailsResponsePOJO ?= null
     var isFromTrainingPath : Boolean = false

}