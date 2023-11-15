package edu.uw.ischool.shiina12.quizdroid

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
    fun setDataURL(newURL: String)
    fun getDataURL(): String
    fun loadTopicsFromURL()
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
class TopicRepositoryList() : TopicRepository {
    private var topics = mutableListOf<Topic>()
    private var topicNames = mutableListOf<String>()

//    private var downloadURL = "https://tednewardsandbox.site44.com/questions.json"
    private var downloadURL: String

    init {
//        downloadURL = "https://raw.githubusercontent.com/shiinasugioka/quizdroid/storage/app/data/quiz_data_formatted.json"
        downloadURL = "https://tednewardsandbox.site44.com/questions.json"
        loadTopicsFromURL()
    }

    override fun loadTopicsFromURL() {
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val urlConnection = URL(downloadURL).openConnection() as HttpURLConnection
            Log.i(TAG, "URL in topicRepo: $downloadURL")
            val inputStream: InputStream = urlConnection.inputStream
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            parseJsonString(jsonString)
        }
    }

    private fun parseJsonString(jsonString: String) {
        val updateList = mutableListOf<Topic>()
        topics.clear()
        topicNames.clear()
        Log.i(TAG, "cleared topicNames array")

        try {
            updateList.clear()
            val listOfTopics = JSONArray(jsonString)

            for (i in 0 until listOfTopics.length()) {
                val topicObj = listOfTopics.getJSONObject(i)

                val title = topicObj.getString("title")
                val desc = topicObj.getString("desc")
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

    override fun getAllTopics(): List<Topic> {
        return topics
    }

    override fun getTopicNames(): List<String> {
        return topicNames
    }

    override fun getTopicByName(topicName: String): Topic? {
        return topics.find { it.title == topicName }
    }

    override fun setDataURL(newURL: String) {
        downloadURL = newURL
        Log.i(TAG, "set new URL in topicRepo: $downloadURL")
    }

    override fun getDataURL(): String {
        return downloadURL
    }
}