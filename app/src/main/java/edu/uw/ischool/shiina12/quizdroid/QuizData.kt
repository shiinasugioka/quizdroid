package edu.uw.ischool.shiina12.quizdroid

object QuizData {
    var correctAnswerCount: Int = 0
    var questionNumber: Int = 1
    var topicName: String = ""
    var numTotalQuestions: Int = 0

    fun reset() {
        correctAnswerCount = 0
        questionNumber = 1
        topicName = ""
        numTotalQuestions = 0
    }

    fun setNewNumTotalQuestions(questionCount: Int) {
        numTotalQuestions = questionCount
    }

    fun setNewTopicName(newTopic: String) {
        topicName = newTopic
    }

    fun incrementCorrectAnswerCount() {
        correctAnswerCount++
    }

    fun incrementQuestionNumber() {
        questionNumber++
    }
}