package edu.uw.ischool.shiina12.quizdroid

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.InputStream
import java.io.Serializable

private const val TAG = "TopicRepo"

interface TopicRepository {
    fun getAllTopics(): List<Topic>
    fun getTopicByName(topicName: String): Topic?
}

data class Quiz(
    val question: String,
    val answers: List<String>,
    val correctAnswer: Int
) : Serializable

data class Topic(
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val listOfQuestions: List<Quiz>
) : Serializable

// implementation that simply stores elements in memory
// from a hard-coded list initialized on startup
class TopicRepositoryList(context: Context) : TopicRepository {
    private val topics = mutableListOf<Topic>()
    private val assets = context.assets

    init {
        val inputStream: InputStream = assets.open("quiz_data.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonRoot = JSONObject(jsonString)
        val topics = jsonRoot.getJSONArray("topics")

        for (i in 0 until topics.length()) {
            val jsonTopic = topics.getJSONObject(i)
            val jsonQuestions = jsonTopic.getJSONArray("questions")
            val listOfQuestions = mutableListOf<Quiz>()

            for (i in 0 until jsonQuestions.length()) {
                val jsonArrQuestions = jsonQuestions.getJSONObject(i).getJSONArray("options")
                val questionOptions = mutableListOf<String>()
                for (i in 0 until jsonArrQuestions.length()) {
                    questionOptions.add(jsonArrQuestions.getString(i))
                }

                val quizItem = Quiz(
                    jsonQuestions.getJSONObject(i).getString("question"),
                    questionOptions,
                    jsonQuestions.getJSONObject(i).getInt("correctAnswer")
                )

                listOfQuestions.add(quizItem)
            }

            val topicItem = Topic(
                jsonTopic.getString("name"),
                jsonTopic.getString("shortDescription"),
                jsonTopic.getString("longDescription"),
                listOfQuestions
            )

            topics.put(topicItem)
        }

        inputStream.close()
    }

    override fun getAllTopics(): List<Topic> {
        return topics
    }

    override fun getTopicByName(topicName: String): Topic? {
        Log.i(TAG, "getTopicName: ${topics.find { it.title == topicName }}")
        return topics.find { it.title == topicName }
    }
}