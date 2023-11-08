package edu.uw.ischool.shiina12.quizdroid

import android.content.Context
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
        val listOfTopics = jsonRoot.getJSONArray("topics")

        for (i in 0 until listOfTopics.length()) {
            val jsonTopic = listOfTopics.getJSONObject(i)
            val jsonQuestions = jsonTopic.getJSONArray("questions")
            val listOfQuestions = mutableListOf<Quiz>()

            for (j in 0 until jsonQuestions.length()) {
                val jsonArrQuestions = jsonQuestions.getJSONObject(j).getJSONArray("options")
                val questionOptions = mutableListOf<String>()
                for (k in 0 until jsonArrQuestions.length()) {
                    questionOptions.add(jsonArrQuestions.getString(k))
                }

                val quizItem = Quiz(
                    jsonQuestions.getJSONObject(j).getString("question"),
                    questionOptions,
                    jsonQuestions.getJSONObject(j).getInt("correctAnswer")
                )

                listOfQuestions.add(quizItem)
            }

            val topicItem = Topic(
                jsonTopic.getString("name"),
                jsonTopic.getString("shortDescription"),
                jsonTopic.getString("longDescription"),
                listOfQuestions
            )

            topics.add(topicItem)
        }

        inputStream.close()
    }

    override fun getAllTopics(): List<Topic> {
        return topics
    }

    override fun getTopicByName(topicName: String): Topic? {
        return topics.find { it.title == topicName }
    }
}