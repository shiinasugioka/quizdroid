package edu.uw.ischool.shiina12.quizdroid

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.InputStream

private const val TAG = "TopicRepo"

interface TopicRepository {
    fun getAllTopics(): List<Topic>
    fun getTopicById(topicId: Int): Topic?
}

data class Quiz(
    val question: String,
    val answers: List<String>,
    val correctAnswer: Int
)

data class Topic(
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val listOfQuestions: List<Quiz>
)

// implementation that simply stores elements in memory
// from a hard-coded list initialized on startup
class TopicRepositoryList(context: Context) : TopicRepository {
    private val topics = mutableListOf<Topic>()
    private val assets = context.assets

    init {
        Log.d(TAG, "topic repo started")
        val inputStream: InputStream = assets.open("quiz_data.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonRoot = JSONObject(jsonString)
        val topics = jsonRoot.getJSONArray("topics")

        for (i in 0 until topics.length()) {
            val jsonTopic = topics.getJSONObject(i)
            val jsonQuestions = jsonTopic.getJSONArray("questions")
            val ListOfQuestions = mutableListOf<Quiz>()

            Log.i(TAG, jsonQuestions.toString())
//            for (i in 0 until jsonQuestions.length()) {
//                val quizItem = Quiz(
//                    jsonQuestions[i].toString(),
//                    jsonQuestions[i].getArray("options"),
//                    jsonQuestions[i].toString()
//                )
//                ListOfQuestions.add(quizItem)
//            }

//            [
//                { 
//                    "question":"What is the derivative of f(x) = 3x^2 - 2x + 5 with respect to x?",
//                    "options":["6x - 2", "6x - 1", "6x + 2", "3x^3 - x^2 + 5x"],
//                    "correctAnswer":0
//                },
//                { "question":"If a triangle has angles measuring 30 degrees, 60 degrees, and x degrees, what is the value of x?", "options":["45 degrees", "60 degrees", "90 degrees", "120 degrees"], "correctAnswer":2 }, { "question":"Solve the equation: 2x - 5 = 7.", "options":["x = -1", "x = 3", "x = 6", "x = 12"], "correctAnswer":2 }, { "question":"What is the value of √(16\/9) + 1\/3?", "options":["5\/3", "4\/3", "1", "4"], "correctAnswer":0 }, { "question":"If a circle has a radius of 5 units, what is its circumference?", "options":["10π units", "15π units", "25 units", "2π units"], "correctAnswer":0 }, { "question":"Evaluate the integral: ∫(2x^2 + 3x - 1) dx.", "options":["x^3 + (3\/2)x^2 - x + C", "x^3 + 3x^2 + x + C", "x^2 + 3x - x^(-1) + C", "(2\/3)x^3 + (3\/2)x^2 - x + C"], "correctAnswer":3 }, { "question":"What is the value of sin(60 degrees)?", "options":["sqrt(2)\/2", "1\/2", "sqrt(3)\/2", "3\/2"], "correctAnswer":2 }]


            val topicItem = Topic(
                jsonTopic.getString("name"),
                jsonTopic.getString("description"),
                jsonTopic.getString("description"),
                ListOfQuestions
            )

//            val topic = topics.getJSONObject(i)
//
//            if (topic.getString("name") == QuizData.topicName) {
//                quizDescription.text = topic.getString("description")
//                val numQuestions = topic.getJSONArray("questions").length()
//                QuizData.setNewNumTotalQuestions(numQuestions)
//                val quizTotalQuestionsTextView = "Total Questions: ${QuizData.numTotalQuestions}"
//                quizTotalQuestions.text = quizTotalQuestionsTextView
//            }
        }

        inputStream.close()
    }

    override fun getAllTopics(): List<Topic> {
        TODO("Not yet implemented")
    }

    override fun getTopicById(topicId: Int): Topic? {
        return topics[topicId]
    }
}