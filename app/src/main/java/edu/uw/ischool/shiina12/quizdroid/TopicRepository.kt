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
    fun getTopicNames(): List<String>
    fun getTopicByName(topicName: String): Topic?
    fun loadTopicsFromURL(url: String)
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

    private val downloadURL: String

    //    private val defaultURLString = "https://tednewardsandbox.site44.com/questions.json"
    private val defaultURLString =
        "https://raw.githubusercontent.com/shiinasugioka/quizdroid/storage/app/data/quiz_data_formatted.json"

    private val downloadInterval: Int
    private val defaultDownloadInterval = 60

    init {
        Log.d(TAG, "started init TopicRepo")
        AppPreferences.initialize(context)

        downloadURL = AppPreferences.getURL("downloadURL", defaultURLString).toString()
        downloadInterval =
            AppPreferences.getDownloadInterval("downloadInterval", defaultDownloadInterval)

        Log.i(TAG, "AppPref initialized: $downloadURL")

        loadTopicsFromURL()
    }

    override fun loadTopicsFromURL(url: String) {
        Log.d(TAG, "started loadTopicsFromURL")
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            Log.i(TAG, "1")
            val urlConnection = URL(downloadURL).openConnection() as HttpURLConnection
            Log.i(TAG, "here")
            val inputStream: InputStream = urlConnection.inputStream
            Log.i(TAG, "here 2")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            Log.i(TAG, "2")

            val updateList = mutableListOf<Topic>()
            Log.i(TAG, "3")
            topics.clear()
            topicNames.clear()
            Log.i(TAG, "4")
            Log.i(TAG, "cleared topicNames array")
            Log.i(TAG, "5")

            try {
                val listOfTopics = JSONArray(jsonString)

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

                    topicNames.add(topicObj.getString("title"))

                    val topicItem = Topic(
                        title,
                        desc,
                        desc,
                        listOfQuestions
                    )

                    updateList.add(topicItem)
                }

                topics = updateList
                Log.i(TAG, "final topicNames in topicRepo: $topicNames")
            } catch (e: JSONException) {
                Log.e(TAG, "JSON invalid: $e")
            }
        }
    }

    override fun getAllTopics(): List<Topic> {
        Log.d(TAG, "started getAllTopics")
        return topics
    }

    override fun getTopicNames(): List<String> {
        Log.d(TAG, "started getTopicNames: $topicNames")
        return topicNames
    }

    override fun getTopicByName(topicName: String): Topic? {
        Log.d(TAG, "started getTopicByName")
        return topics.find { it.title == topicName }
    }
}