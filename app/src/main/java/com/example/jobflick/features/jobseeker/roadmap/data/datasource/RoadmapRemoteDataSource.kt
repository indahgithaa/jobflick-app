package com.example.jobflick.features.jobseeker.roadmap.data.datasource

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.example.jobflick.core.network.PocketBaseHttp
import com.example.jobflick.core.network.Record
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.features.jobseeker.roadmap.domain.model.*
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

class RoadmapRemoteDataSource(
    private val pb: PocketBaseHttp,
    private val roadmapsCollection: String,
    private val modulesCollection: String,
    private val articlesCollection: String,
    private val quizzesCollection: String,
    private val questionsCollection: String,
    private val optionsCollection: String,
    private val moduleProgressCollection: String,
    private val quizProgressCollection: String,
    private val articleProgressCollection: String
) {

    // =====================================================================
    // Public API seolah-olah ini endpoint HTTP
    // =====================================================================

    suspend fun getAvailableRoles(): List<String> {
        val response = pb.getList(roadmapsCollection)
        return response.items.mapNotNull { it.data["role"]?.jsonPrimitive?.content }.distinct()
    }

    suspend fun getRoadmapRole(roleName: String): RoadmapRole {
        val authResponse = pb.authRefresh(collection = "_pb_users_auth_")
        val userId = authResponse.record.id

        val roadmaps = pb.getList(roadmapsCollection, filter = "role=\"$roleName\"")
        val roadmapRecord = roadmaps.items.firstOrNull()
            ?: throw Exception("Roadmap not found for role: $roleName")

        val roadmapId = roadmapRecord.id

        val moduleIds = roadmapRecord.data["modules"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
        val filter = moduleIds.joinToString(" || ") { "id = '$it'" }
        val modulesResponse = pb.getList(modulesCollection, filter = filter, expand = "articles,quizzes.questions.options")
        val allModules = modulesResponse.items.associateBy { it.id }

        val modules = moduleIds.map { moduleId ->
            val moduleRecord = allModules[moduleId] ?: throw Exception("Module not found: $moduleId")

            val articleIds = moduleRecord.data["articles"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
            val articlesExpandJson = moduleRecord.expand?.get("articles")?.jsonArray
            val articlesExpand: List<Record> = articlesExpandJson?.map { pb.json.decodeFromJsonElement(Record.serializer(), it) } ?: emptyList()
            val articles = articleIds.map { articleId ->
                val article = articlesExpand.firstOrNull { it.id == articleId } ?: pb.getOne(articlesCollection, articleId)
                RoadmapArticle(
                    id = article.id,
                    title = article.data["title"]?.jsonPrimitive?.content ?: "",
                    content = article.data["content"]?.jsonPrimitive?.content ?: "",
                    order = article.data["order"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0,
                    updatedAt = article.data["updated"]?.jsonPrimitive?.content ?: ""
                )
            }.sortedBy { it.order }

            val quizIds = moduleRecord.data["quizzes"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
            val quiz = if (quizIds.isNotEmpty()) {
                val quizId = quizIds.first()
                val quizzesExpandJson = moduleRecord.expand?.get("quizzes")?.jsonArray
                val quizzesExpand: List<Record> = quizzesExpandJson?.map { pb.json.decodeFromJsonElement(Record.serializer(), it) } ?: emptyList()
                val quizRecord = quizzesExpand.firstOrNull { it.id == quizId } ?: pb.getOne(quizzesCollection, quizId, expand = "questions.options")
                val questionIds = quizRecord.data["questions"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()

                val questionsExpandJson = quizRecord.expand?.get("questions")?.jsonArray
                val questionsExpand: List<Record> = questionsExpandJson?.map { pb.json.decodeFromJsonElement(Record.serializer(), it) } ?: emptyList()
                val questions = questionIds.map { questionId ->
                    val questionRecord = questionsExpand.firstOrNull { it.id == questionId } ?: pb.getOne(questionsCollection, questionId)
                    val optionIds = questionRecord.data["options"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
                    val optionsExpandJson = questionRecord.expand?.get("options")?.jsonArray
                    val optionsExpand: List<Record> = optionsExpandJson?.map { pb.json.decodeFromJsonElement(Record.serializer(), it) } ?: emptyList()
                    val options = optionIds.map { optionId ->
                        val option = optionsExpand.firstOrNull { it.id == optionId } ?: pb.getOne(optionsCollection, optionId)
                        option.data["content"]?.jsonPrimitive?.content ?: ""
                    }
                    val correctIndex = optionIds.indexOfFirst { optionId ->
                        val option = optionsExpand.firstOrNull { it.id == optionId } ?: pb.getOne(optionsCollection, optionId)
                        option.data["isCorrect"]?.jsonPrimitive?.content?.toBoolean() ?: false
                    }
                    QuizQuestion(
                        id = questionRecord.id,
                        question = questionRecord.data["content"]?.jsonPrimitive?.content ?: "",
                        options = options,
                        correctOptionIndex = correctIndex
                    )
                }
                Pair(quizRecord.data["title"]?.jsonPrimitive?.content ?: "", questions)
            } else {
                Pair("", emptyList())
            }

            val colorHex = moduleRecord.data["color"]?.jsonPrimitive?.content ?: "#000000"
            val color = try {
                Color(colorHex.toColorInt())
            } catch (e: IllegalArgumentException) {
                BluePrimary // fallback
            }

            val learningPoints = moduleRecord.data["learningPoints"]?.jsonPrimitive?.content?.split(";")?.map { it.trim() } ?: emptyList()

            RoadmapModule(
                id = moduleRecord.id,
                number = moduleRecord.data["order"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0,
                title = moduleRecord.data["title"]?.jsonPrimitive?.content ?: "",
                description = moduleRecord.data["description"]?.jsonPrimitive?.content ?: "",
                progress = 0f,
                color = color,
                learningPoints = learningPoints,
                articles = articles,
                quizTitle = quiz.first,
                questions = quiz.second,
                status = RoadmapModuleStatus.LOCKED,
                updatedAt = moduleRecord.data["updated"]?.jsonPrimitive?.content ?: ""
            )
        }.sortedBy { it.number }

        val moduleProgressResponse = pb.getList(moduleProgressCollection, filter = "user=\"$userId\"")
        val completedModuleIds = moduleProgressResponse.items.map { it.data["module"]?.jsonPrimitive?.content }.toSet()

        val articleProgressResponse = pb.getList(articleProgressCollection, filter = "user=\"$userId\"")
        val readArticleIds = articleProgressResponse.items.map { it.data["article"]?.jsonPrimitive?.content }.toSet()

        val quizProgressResponse = pb.getList(quizProgressCollection, filter = "user=\"$userId\"")
        val completedQuizIds = quizProgressResponse.items.map { it.data["quiz"]?.jsonPrimitive?.content }.toSet()

        val moduleToQuizIds = allModules.mapValues { it.value.data["quizzes"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList() }

        val modulesWithStatus = modules.mapIndexed { index, module ->
            val moduleId = module.id
            val quizIds = moduleToQuizIds[moduleId] ?: emptyList()
            val articleIds = module.articles.map { it.id }
            val readArticlesCount = articleIds.count { readArticleIds.contains(it) }
            val quizCompleted = quizIds.isNotEmpty() && completedQuizIds.contains(quizIds.first())
            val totalItems = articleIds.size + (if (quizIds.isNotEmpty()) 1 else 0)
            val completedItems = readArticlesCount + (if (quizCompleted) 1 else 0)
            val progress = if (totalItems > 0) completedItems.toFloat() / totalItems else 0f

            val isCompleted = progress == 1f
            val isFirst = index == 0
            val prevModule = modules.getOrNull(index - 1)
            val prevCompleted = if (prevModule != null) {
                val prevModuleId = prevModule.id
                val prevQuizIds = moduleToQuizIds[prevModuleId] ?: emptyList()
                val prevArticleIds = prevModule.articles.map { it.id }
                val prevReadCount = prevArticleIds.count { readArticleIds.contains(it) }
                val prevQuizCompleted = prevQuizIds.isNotEmpty() && completedQuizIds.contains(prevQuizIds.first())
                val prevTotal = prevArticleIds.size + (if (prevQuizIds.isNotEmpty()) 1 else 0)
                val prevCompletedItems = prevReadCount + (if (prevQuizCompleted) 1 else 0)
                (prevCompletedItems.toFloat() / prevTotal) == 1f
            } else false

            val status = when {
                isCompleted -> RoadmapModuleStatus.COMPLETED
                isFirst || prevCompleted -> RoadmapModuleStatus.IN_PROGRESS
                else -> RoadmapModuleStatus.LOCKED
            }

            module.copy(status = status, progress = progress)
        }

        val overallProgress = if (modulesWithStatus.isNotEmpty()) {
            modulesWithStatus.count { it.status == RoadmapModuleStatus.COMPLETED }.toFloat() / modulesWithStatus.size
        } else 0f

        return RoadmapRole(
            id = roadmapId,
            name = roleName,
            progress = overallProgress,
            modules = modulesWithStatus,
            availableRoles = getAvailableRoles(),
            updatedAt = roadmapRecord.data["updated"]?.jsonPrimitive?.content ?: ""
        )
    }

    suspend fun calculateQuizScore(
        roleName: String,
        moduleId: String,
        answers: List<Int>
    ): Int {
        val authResponse = pb.authRefresh(collection = "_pb_users_auth_")
        val userId = authResponse.record.id

        val moduleRecord = pb.getOne(modulesCollection, moduleId)
        val quizIds = moduleRecord.data["quizzes"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
        if (quizIds.isEmpty()) return 0

        val quizId = quizIds.first()
        val quizRecord = pb.getOne(quizzesCollection, quizId, expand = "questions.options")
        val questionIds = quizRecord.data["questions"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()

        val questionsExpandJson = quizRecord.expand?.get("questions")?.jsonArray
        val questionsExpand: List<Record> = questionsExpandJson?.map { pb.json.decodeFromJsonElement(Record.serializer(), it) } ?: emptyList()

        val correctIndices = questionIds.map { questionId ->
            val questionRecord = questionsExpand.firstOrNull { it.id == questionId } ?: pb.getOne(questionsCollection, questionId)
            val optionIds = questionRecord.data["options"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
            val optionsExpandJson = questionRecord.expand?.get("options")?.jsonArray
            val optionsExpand: List<Record> = optionsExpandJson?.map { pb.json.decodeFromJsonElement(Record.serializer(), it) } ?: emptyList()
            optionIds.indexOfFirst { optionId ->
                val option = optionsExpand.firstOrNull { it.id == optionId } ?: pb.getOne(optionsCollection, optionId)
                option.data["isCorrect"]?.jsonPrimitive?.content?.toBoolean() ?: false
            }
        }

        var correct = 0
        correctIndices.forEachIndexed { index, correctIdx ->
            val userAnswer = answers.getOrNull(index)
            if (userAnswer == correctIdx) correct++
        }

        val score = (correct.toFloat() / correctIndices.size * 100).toInt()

        if (score >= 60) {
            // Insert quizProgress
            val data = buildJsonObject {
                put("user", JsonPrimitive(userId))
                put("quiz", JsonPrimitive(quizId))
            }
            pb.create(quizProgressCollection, data)
            pb.clearCache()

            // Refetch completedQuizIds
            val completedQuizIds = pb.getList(quizProgressCollection, filter = "user=\"$userId\"").items.map { it.data["quiz"]?.jsonPrimitive?.content }.toSet()

            // Check all articles read
            val articleIds = moduleRecord.data["articles"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
            val articleProgressResponse = pb.getList(articleProgressCollection, filter = "user=\"$userId\"")
            val readArticleIds = articleProgressResponse.items.map { it.data["article"]?.jsonPrimitive?.content }.toSet()
            val allArticlesRead = articleIds.all { readArticleIds.contains(it) }

            if (quizIds.all { completedQuizIds.contains(it) } && allArticlesRead) {
                // Insert moduleProgress
                val moduleData = buildJsonObject {
                    put("user", JsonPrimitive(userId))
                    put("module", JsonPrimitive(moduleId))
                }
                pb.create(moduleProgressCollection, moduleData)
                pb.clearCache()
            }
        }

        return score
    }

    suspend fun markArticleAsRead(articleId: String) {
        val authResponse = pb.authRefresh(collection = "_pb_users_auth_")
        val userId = authResponse.record.id

        // Insert article progress
        val data = buildJsonObject {
            put("user", JsonPrimitive(userId))
            put("article", JsonPrimitive(articleId))
        }
        pb.create(articleProgressCollection, data)
    }
}
