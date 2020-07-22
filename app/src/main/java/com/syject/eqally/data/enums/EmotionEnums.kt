package com.syject.eqally.data.enums

import com.syject.eqally.R
import java.io.Serializable

enum class EmotionEnums(
    val id: Int,
    val emotionName: Int,
    val emotionFile: String,
    val emotionDescription: Int,
    val emotionTrigger: Int,
    val emotionPurpose: Int,
    val emotionBody: Int,
    val emotionFeel: Int,
    val emotionSound: Int,
    val emotionCards: Int,
    val emotionAchievementEnum: AchievementEnum?
) : Serializable {

    NEUTRAL(
        0,
        0,
        "LEARN.1/17_0_0.enc",
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        null
    ),

    SURPRISE(
        1,
        R.string.Emotion_surprise,
        "LEARN.1/17_1_0.enc",
        R.string.Emotion_surprise_description,
        R.string.Emotion_surprise_trigger,
        R.string.Emotion_surprise_purpose,
        R.string.Emotion_surprise_body_reaction,
        R.string.Emotion_surprise_feel,
        R.string.Emotion_surprise_sound,
        R.array.Emotion_surprise_cards,
        AchievementEnum.SURPRISE
    ),

    FEAR(
        2,
        R.string.Emotion_fear,
        "LEARN.1/17_2_0.enc",
        R.string.Emotion_fear_description,
        R.string.Emotion_fear_trigger,
        R.string.Emotion_fear_purpose,
        R.string.Emotion_fear_body_reaction,
        R.string.Emotion_fear_feel,
        R.string.Emotion_fear_sound,
        R.array.Emotion_fear_cards,
        AchievementEnum.FEAR
    ),

    ANGER(
        3,
        R.string.Emotion_anger,
        "LEARN.1/17_3_0.enc",
        R.string.Emotion_anger_description,
        R.string.Emotion_anger_trigger,
        R.string.Emotion_anger_purpose,
        R.string.Emotion_anger_body_reaction,
        R.string.Emotion_anger_feel,
        R.string.Emotion_anger_sound,
        R.array.Emotion_anger_cards,
        AchievementEnum.ANGER
    ),

    DISGUST(
        4,
        R.string.Emotion_disgust,
        "LEARN.1/17_4_0.enc",
        R.string.Emotion_disgust_description,
        R.string.Emotion_disgust_trigger,
        R.string.Emotion_disgust_purpose,
        R.string.Emotion_disgust_body_reaction,
        R.string.Emotion_disgust_feel,
        R.string.Emotion_disgust_sound,
        R.array.Emotion_disgust_cards,
        AchievementEnum.DISGUST
    ),

    SADNESS(
        5,
        R.string.Emotion_sadness,
        "LEARN.1/17_5_0.enc",
        R.string.Emotion_sadness_description,
        R.string.Emotion_sadness_trigger,
        R.string.Emotion_sadness_purpose,
        R.string.Emotion_sadness_body_reaction,
        R.string.Emotion_sadness_feel,
        R.string.Emotion_sadness_sound,
        R.array.Emotion_sadness_cards,
        AchievementEnum.SADNESS
    ),

    CONTEMPT(
        6,
        R.string.Emotion_contempt,
        "LEARN.1/17_6_0.enc",
        R.string.Emotion_contempt_description,
        R.string.Emotion_contempt_trigger,
        R.string.Emotion_contempt_purpose,
        R.string.Emotion_contempt_body_reaction,
        R.string.Emotion_contempt_feel,
        R.string.Emotion_contempt_sound,
        R.array.Emotion_contempt_cards,
        AchievementEnum.CONTEMPT
    ),

    HAPPINESS(
        7,
        R.string.Emotion_happiness,
        "LEARN.1/17_7_0.enc",
        R.string.Emotion_happiness_description,
        R.string.Emotion_happiness_trigger,
        R.string.Emotion_happiness_purpose,
        R.string.Emotion_happiness_body_reaction,
        R.string.Emotion_happiness_feel,
        R.string.Emotion_happiness_sound,
        R.array.Emotion_happiness_cards,
        AchievementEnum.HAPPINESS
    );

    companion object {
        fun getEmotionByID(id: Int) = values()[id]
    }
}