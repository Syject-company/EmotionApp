package com.syject.eqally.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import com.syject.eqally.data.enums.EmotionEnums
import com.syject.eqally.data.enums.TestEnums
import org.cryptonode.jncryptor.AES256JNCryptor
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap

class AppUtils {

    companion object {

        fun groupEmotionsFiles(emotionsFiles: Array<out String>): TreeMap<Int, MutableList<String>> {
            val emotionsHasMap = TreeMap<Int, MutableList<String>>()
            emotionsFiles.forEach { emotionsFile ->
                val emotionIndex = getNumberByPosition(emotionsFile, 1)
                if (!emotionsHasMap.containsKey(emotionIndex)) {
                    emotionsHasMap[emotionIndex] = mutableListOf()
                }
                emotionsHasMap[emotionIndex]!!.add(emotionsFile)
            }
            return emotionsHasMap
        }

        fun decodeImages(activity: AppCompatActivity, file: String, listener: DecodeListener) {
            Thread {
                run {
                    val bitmap = decodeImages(activity.assets.open(file))
                    activity.runOnUiThread { listener.onDecode(bitmap) }
                }
            }.start()
        }

        private fun decodeImages(inputStream: InputStream): Bitmap {
            val bytes = ByteArray(inputStream.available())
            val buf = BufferedInputStream(inputStream)
            buf.read(bytes, 0, bytes.size)
            buf.close()
            val result = AES256JNCryptor().decryptData(bytes, "78hiigw7f6870527ghbi".toCharArray())
            return BitmapFactory.decodeStream(
                ByteArrayInputStream(result),
                null,
                BitmapFactory.Options().apply { inSampleSize = 2 })!!
        }

        fun applyTestLimit(
            testEnums: TestEnums,
            groupTestImages: TreeMap<Int, MutableList<String>>
        ): TreeMap<Int, MutableList<String>> {
            val limitTestFiles = TreeMap<Int, MutableList<String>>()
            val presentImages = HashMap<Int, Int>()
            limitTestFiles[EmotionEnums.NEUTRAL.id] =
                groupTestImages.remove(EmotionEnums.NEUTRAL.id)!!
            groupTestImages.forEach { (emotionID, emotionFiles) ->
                for (i in 1..testEnums.maxEmotions) {
                    if (!limitTestFiles.containsKey(emotionID)) {
                        limitTestFiles[emotionID] = mutableListOf()
                    } else if (limitTestFiles[emotionID]!!.size == testEnums.maxEmotions) {
                        return@forEach
                    }

                    if (emotionFiles.size > 0) {
                        val emotionsFile =
                            getRandomEmotionFile(presentImages, emotionFiles, testEnums.maxImages)
                        if (emotionsFile != null) {
                            val imageIndex = getNumberByPosition(emotionsFile, 0)
                            if (!presentImages.containsKey(imageIndex)) {
                                presentImages[imageIndex] = 0
                            }
                            presentImages[imageIndex] = presentImages[imageIndex]!! + 1
                            limitTestFiles[emotionID]!!.add(emotionsFile)
                        }
                    }
                }
            }
            return limitTestFiles
        }

        private fun getRandomEmotionFile(
            presentImages: HashMap<Int, Int>,
            emotions: MutableList<String>,
            maxImages: Int
        ): String? {
            if (emotions.size == 0) return null
            val emotionFile = emotions.removeAt((0 until emotions.size).random())
            val imageIndex = getNumberByPosition(emotionFile, 0)
            return if (!presentImages.containsKey(imageIndex) || presentImages[imageIndex]!! != maxImages)
                emotionFile
            else getRandomEmotionFile(presentImages, emotions, maxImages)
        }

        fun getNumberByPosition(fileName: String, position: Int): Int {
            return fileName
                .substring(0, fileName.indexOf("."))
                .split("_")[position]
                .toInt()
        }
    }

    interface DecodeListener {
        fun onDecode(bitmap: Bitmap)
    }
}