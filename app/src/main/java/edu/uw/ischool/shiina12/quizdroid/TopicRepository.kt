package edu.uw.ischool.shiina12.quizdroid

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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
    private var topics = mutableListOf<Topic>()

    //    private val assets = context.assets
    private var downloadURL = "https://tednewardsandbox.site44.com/questions.json"

    init {
        loadTopicsFromURL()
    }

    private fun loadTopicsFromURL() {
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val urlConnection = URL(downloadURL).openConnection() as HttpURLConnection
            val inputStream: InputStream = urlConnection.inputStream
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            parseJsonString(jsonString)
        }
    }

    private fun parseJsonString(jsonString: String) {
        try {
            val listOfTopics = JSONArray(jsonString)
//            val listOfTopics = jsonRoot.getJSONArray("topics")


            val updateList = mutableListOf<Topic>()

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

                updateList.add(topicItem)
            }

            topics = updateList
        } catch (e: JSONException) {
            Log.e(TAG, "JSON invalid: $e")
        }

    }

    override fun getAllTopics(): List<Topic> {
        return topics
    }

    override fun getTopicByName(topicName: String): Topic? {
        return topics.find { it.title == topicName }
    }
}