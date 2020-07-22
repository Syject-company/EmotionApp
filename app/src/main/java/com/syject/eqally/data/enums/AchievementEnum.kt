package com.syject.eqally.data.enums

import com.syject.eqally.R

enum class AchievementEnum constructor(
    val imageID: Int,
    val achievementRowCount: Int,
    val achievementName: Int,
    val achievementDescription: Int
) {

    CURIOSITY(
        R.drawable.achievement_curiosity,
        1,
        R.string.Achievement_curiosity,
        R.string.Achievement_curiosity_description
    ),
    KNOWLEDGE(
        R.drawable.achievement_knowledge,
        1,
        R.string.Achievement_knowledge,
        R.string.Achievement_knowledge_description
    ),
    IMPROVEMENT(
        R.drawable.achievement_improvement,
        3,
        R.string.Achievement_boost,
        R.string.Achievement_boost_description
    ),
    MASTER_RECOGNIZER(
        R.drawable.achievement_masterrecognizer,
        1,
        R.string.Achievement_master_recognizer,
        R.string.Achievement_master_recognizer_description
    ),
    SUPER_RECOGNIZER(
        R.drawable.achievement_superrecognizer,
        1,
        R.string.Achievement_super_recognizer,
        R.string.Achievement_super_recognizer_description
    ),

    SURPRISE(
        R.drawable.achievement_suprise,
        3,
        R.string.Emotion_surprise,
        R.string.Achievement_surprise_description
    ),
    FEAR(
        R.drawable.achievement_fear,
        3,
        R.string.Emotion_fear,
        R.string.Achievement_fear_description
    ),
    ANGER(
        R.drawable.ahievement_anger,
        3,
        R.string.Emotion_anger,
        R.string.Achievement_anger_description
    ),
    DISGUST(
        R.drawable.achievement_disgust,
        3,
        R.string.Emotion_disgust,
        R.string.Achievement_disgust_description
    ),
    SADNESS(
        R.drawable.achievement_sadness,
        3,
        R.string.Emotion_sadness,
        R.string.Achievement_sadness_description
    ),
    CONTEMPT(
        R.drawable.achievement_contempt,
        3,
        R.string.Emotion_contempt,
        R.string.Achievement_contempt_description
    ),
    HAPPINESS(
        R.drawable.achievement_happiness,
        3,
        R.string.Emotion_happiness,
        R.string.Achievement_happiness_description
    )
}