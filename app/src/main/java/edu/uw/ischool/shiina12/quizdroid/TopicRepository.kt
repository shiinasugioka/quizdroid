package edu.uw.ischool.shiina12.quizdroid

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import java.io.InputStream
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val TAG = "TopicRepo"

interface TopicRepository {
    fun getAllTopics(): List<Topic>
    fun getTopicByName(topicName: String): Topic?
    fun loadTopicsFromURL(url: String)
    fun getTopicNames(): List<String>
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

class TopicRepositoryList(context: Context) : TopicRepository {
    private var topics = mutableListOf<Topic>()
    private var topicNames = mutableListOf<String>()

    private val downloadURL: String
    private val downloadInterval: Int

    init {
        Log.d(TAG, "started init TopicRepo")
        AppPreferences.initialize(context)

        downloadURL = AppPreferences.getURL("downloadURL").toString()
        downloadInterval =
            AppPreferences.getDownloadInterval("downloadInterval")

        loadTopicsFromURL(downloadURL)
    }

    override fun loadTopicsFromURL(url: String) {
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            val inputStream: InputStream = urlConnection.inputStream
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            try {
                topics.clear()
                parseJSON(jsonString)
            } catch (e: JSONException) {
                Log.e(TAG, "JSON invalid: $e")
            }
        }
    }

    private fun parseJSON(jsonString: String) {
        val listOfTopics = JSONArray(jsonString)
        val updateList = mutableListOf<Topic>()
        val updateNameList = mutableListOf<String>()

        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            for (i in 0 until listOfTopics.length()) {
                val topicObj = listOfTopics.getJSONObject(i)

                val title = topicObj.getString("title")
                val desc = topicObj.getString("desc")
                val jsonQuestions = topicObj.getJSONArray("questions")
                val listOfQuestions = mutableListOf<Quiz>()

                for (j in 0 until jsonQuestions.length()) {
                    val jsonArrQuestions =
                        jsonQuestions.getJSONObject(j).getJSONArray("answers")
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

                updateNameList.add(topicObj.getString("title"))

                val topicItem = Topic(
                    title,
                    desc,
                    desc,
                    listOfQuestions
                )

                updateList.add(topicItem)
            }

            topics = updateList
            topicNames = updateNameList
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