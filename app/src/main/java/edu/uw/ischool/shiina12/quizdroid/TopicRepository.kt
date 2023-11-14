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
    fun getTopicNames(): List<String>
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
    private var topicNames = mutableListOf<String>()

    private var downloadURL = "https://tednewardsandbox.site44.com/questions.json"
    // Extra Credit
//    private var downloadURL = "https://raw.githubusercontent.com/shiinasugioka/quizdroid/storage/app/data/quiz_data_formatted.json"

    init {
        loadTopicsFromURL()
    }

    private fun loadTopicsFromURL() {
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val urlConnection = URL(downloadURL).openConnection() as HttpURLConnection
            val inputStream: InputStream = urlConnection.inputStream
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            Log.i(TAG, jsonString)
            parseJsonString(jsonString)
        }
    }

    private fun parseJsonString(jsonString: String) {
        val updateList = mutableListOf<Topic>()
        try {
            val listOfTopics = JSONArray(jsonString)
            Log.i(TAG, "list of topics len: ${listOfTopics.length()}")
            Log.i(TAG, "first title: ${listOfTopics.getJSONObject(0).getString("title")}")
            Log.i(TAG, "second title: ${listOfTopics.getJSONObject(1).getString("title")}")
            Log.i(TAG, "third title: ${listOfTopics.getJSONObject(2).getString("title")}")

            for (i in 0 until listOfTopics.length()) {
                val topicObj = listOfTopics.getJSONObject(i)
                Log.i(TAG, "index: $i")

                val jsonQuestions = topicObj.getJSONArray("questions")
                val listOfQuestions = mutableListOf<Quiz>()

                for (j in 0 until jsonQuestions.length()) {
                    val jsonArrQuestions = jsonQuestions.getJSONObject(j).getJSONArray("answers")
                    val questionOptions = mutableListOf<String>()
                    for (k in 0 until jsonArrQuestions.length()) {
                        questionOptions.add(jsonArrQuestions.getString(k))
                    }

                    val correctAns = jsonQuestions.getJSONObject(j).getInt("answer") - 1

                    val quizItem = Quiz(
                        jsonQuestions.getJSONObject(j).getString("text"),
                        questionOptions,
                        correctAns
                    )

                    listOfQuestions.add(quizItem)
                }

                topicNames.add(topicObj.getString("title"))
                Log.i(TAG, "here 1")

                val topicItem = Topic(
                    topicObj.getString("title"),
                    topicObj.getString("desc"),
                    topicObj.getString("desc"),
                    listOfQuestions
                )

                Log.i(TAG, "here 2")

                Log.i(TAG, topicObj.getString("title at index $i"))

                updateList.add(topicItem)
            }

            topics = updateList
            Log.i(TAG, "final topics: $topics")
        } catch (e: JSONException) {
            Log.e(TAG, "JSON invalid: $e")
        }
    }

    override fun getAllTopics(): List<Topic> {
        return topics
    }

    override fun getTopicNames(): List<String> {
        return topicNames
    }

    override fun getTopicByName(topicName: String): Topic? {
        return topics.find { it.title == topicName }
    }
}