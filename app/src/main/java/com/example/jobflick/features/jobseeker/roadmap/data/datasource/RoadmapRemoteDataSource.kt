package com.example.jobflick.features.jobseeker.roadmap.data.datasource

import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.features.jobseeker.roadmap.domain.model.*

class RoadmapRemoteDataSource {

    /**
     * Simulasi state "backend".
     * key   = roleId
     * value = set of moduleId yang sudah completed.
     */
    private val completedModules: MutableMap<String, MutableSet<String>> = mutableMapOf()

    // =====================================================================
    // Public API seolah-olah ini endpoint HTTP
    // =====================================================================

    fun getAvailableRoles(): List<String> = defaultRoleList()

    fun getRoadmapRole(roleName: String): RoadmapRole {
        val baseRole = when (roleName) {
            "Senior Software Engineer"  -> createSeniorSoftwareEngineer()
            "Frontend Developer"        -> createFrontendDeveloper()
            "Machine Learning Engineer" -> createMachineLearningEngineer()
            else                        -> createSeniorSoftwareEngineer()
        }

        val completedForRole = completedModules[baseRole.id] ?: emptySet()

        val modulesWithStatus = baseRole.modules.mapIndexed { index, module ->
            val isFirst = index == 0
            val isCompleted = completedForRole.contains(module.id)
            val prevModuleId = baseRole.modules.getOrNull(index - 1)?.id
            val prevCompleted = prevModuleId != null && completedForRole.contains(prevModuleId)

            val status = when {
                isCompleted -> RoadmapModuleStatus.COMPLETED
                isFirst     -> RoadmapModuleStatus.IN_PROGRESS
                prevCompleted -> RoadmapModuleStatus.IN_PROGRESS
                else        -> RoadmapModuleStatus.LOCKED
            }

            val progress = when (status) {
                RoadmapModuleStatus.COMPLETED   -> 1f
                RoadmapModuleStatus.IN_PROGRESS -> 0.25f // dummy: sudah mulai
                RoadmapModuleStatus.LOCKED      -> 0f
            }

            module.copy(
                status = status,
                progress = progress
            )
        }

        val overallProgress =
            if (modulesWithStatus.isEmpty()) 0f
            else modulesWithStatus.count { it.status == RoadmapModuleStatus.COMPLETED } /
                    modulesWithStatus.size.toFloat()

        return baseRole.copy(
            modules = modulesWithStatus,
            progress = overallProgress
        )
    }

    /**
     * Hitung score kuis dan tandai modul sebagai completed jika lulus.
     * - score = (benar / total) * 100
     * - lulus jika score >= 60
     */
    fun calculateQuizScore(
        roleName: String,
        moduleId: String,
        answers: List<Int>
    ): Int {
        val role = getRoadmapRole(roleName)
        val module = role.modules.firstOrNull { it.id == moduleId } ?: return 0
        val questions = module.questions
        if (questions.isEmpty()) return 0

        var correct = 0
        questions.forEachIndexed { index, question ->
            val userAnswerIndex = answers.getOrNull(index)
            if (userAnswerIndex != null && userAnswerIndex == question.correctOptionIndex) {
                correct++
            }
        }

        val score = (correct.toFloat() / questions.size.toFloat() * 100f).toInt()

        if (score >= 60) {
            val set = completedModules.getOrPut(role.id) { mutableSetOf() }
            set.add(moduleId)
        }

        return score
    }

    // =====================================================================
    // Dummy "database" per role
    // =====================================================================

    // ---------- Role 1: Senior Software Engineer ----------
    private fun createSeniorSoftwareEngineer(): RoadmapRole {

        val module1 = RoadmapModule(
            id = "senior_se_mod1",
            number = 1,
            title = "System Design & Architecture",
            description = "Mempelajari arsitektur sistem skala besar, microservices, CAP theorem, dan strategi scaling.",
            progress = 0f,
            color = BluePrimary,
            learningPoints = listOf(
                "Scalability dan reliability",
                "Microservices vs monolith",
                "Event-driven architecture",
                "CAP theorem & data consistency"
            ),
            articles = listOf(
                RoadmapArticle(
                    id = "senior_se_mod1_art1",
                    title = "Introduction to System Architecture",
                    content = "Gambaran umum arsitektur sistem skala besar dan komponen utamanya.",
                    order = 1,
                    updatedAt = "2024-11-27 10:15:30.123Z"
                ),
                RoadmapArticle(
                    id = "senior_se_mod1_art2",
                    title = "High Availability & Fault Tolerance",
                    content = "Konsep HA, redundancy, dan strategi fault tolerance pada sistem terdistribusi.",
                    order = 2,
                    updatedAt = "2024-11-27 10:16:10.456Z"
                ),
                RoadmapArticle(
                    id = "senior_se_mod1_art3",
                    title = "Caching Strategies for High Traffic",
                    content = "Pola caching (read-through, write-through, write-back) dan pattern cache-aside.",
                    order = 3,
                    updatedAt = "2024-11-27 10:16:40.789Z"
                )
            ),
            quizTitle = "Kuis System Design",
            questions = listOf(
                QuizQuestion(
                    id = "senior_se_mod1_q1",
                    question = "Apa tujuan utama dari penerapan caching?",
                    options = listOf(
                        "Mengurangi biaya lisensi database",
                        "Meningkatkan latency & throughput sistem",
                        "Mempermudah proses deployment",
                        "Menghilangkan kebutuhan load balancer"
                    ),
                    correctOptionIndex = 1
                ),
                QuizQuestion(
                    id = "senior_se_mod1_q2",
                    question = "Apa makna ‘Partition Tolerance’ pada CAP theorem?",
                    options = listOf(
                        "Sistem tetap berjalan meski terjadi pemisahan jaringan",
                        "Database tidak membutuhkan sharding",
                        "Sistem tidak akan pernah mengalami downtime",
                        "Sistem tidak butuh mekanisme retry"
                    ),
                    correctOptionIndex = 0
                )
            ),
            status = RoadmapModuleStatus.LOCKED,
            updatedAt = "2024-11-27 10:17:00.123Z"
        )

        val module2 = RoadmapModule(
            id = "senior_se_mod2",
            number = 2,
            title = "Software Engineering Best Practices",
            description = "Best practices untuk menulis kode berkualitas tinggi, maintainable, dan scalable.",
            progress = 0f,
            color = OrangePrimary,
            learningPoints = listOf(
                "Clean Code & Clean Architecture",
                "Code Review Best Practices",
                "CI/CD Pipeline",
                "Refactoring Techniques"
            ),
            articles = listOf(
                RoadmapArticle(
                    id = "senior_se_mod2_art1",
                    title = "Clean Code Principles",
                    content = "Prinsip dasar clean code, penamaan, fungsi kecil, dan single responsibility.",
                    order = 1,
                    updatedAt = "2024-11-27 10:17:30.123Z"
                ),
                RoadmapArticle(
                    id = "senior_se_mod2_art2",
                    title = "Continuous Integration & Delivery",
                    content = "Konsep CI/CD, pipeline build, test otomatis, dan deployment.",
                    order = 2,
                    updatedAt = "2024-11-27 10:17:40.456Z"
                ),
                RoadmapArticle(
                    id = "senior_se_mod2_art3",
                    title = "Effective Code Reviews",
                    content = "Strategi melakukan code review yang efisien dan tidak menghambat delivery.",
                    order = 3,
                    updatedAt = "2024-11-27 10:17:50.789Z"
                )
            ),
            quizTitle = "Kuis Best Practices",
            questions = listOf(
                QuizQuestion(
                    id = "senior_se_mod2_q1",
                    question = "Apa tujuan utama dari code review?",
                    options = listOf(
                        "Mencari kesalahan kecil pada whitespace",
                        "Meningkatkan kualitas kode dan knowledge sharing",
                        "Menambah waktu pengerjaan feature",
                        "Mengurangi jumlah unit test"
                    ),
                    correctOptionIndex = 1
                )
            ),
            status = RoadmapModuleStatus.LOCKED,
            updatedAt = "2024-11-27 10:18:00.123Z"
        )

        val module3 = RoadmapModule(
            id = "senior_se_mod3",
            number = 3,
            title = "Distributed Systems",
            description = "Konsep fundamental sistem terdistribusi, message queue, load balancing, dan service discovery.",
            progress = 0f,
            color = BluePrimary,
            learningPoints = listOf(
                "Message Queue (Kafka, RabbitMQ)",
                "Load Balancing Techniques",
                "Service Discovery",
                "Eventual Consistency"
            ),
            articles = listOf(
                RoadmapArticle(
                    id = "senior_se_mod3_art1",
                    title = "Introduction to Distributed Systems",
                    content = "Definisi sistem terdistribusi dan tantangan utamanya (latency, failure, konsistensi).",
                    order = 1,
                    updatedAt = "2024-11-27 10:18:20.123Z"
                )
            ),
            quizTitle = "Kuis Distributed Systems",
            questions = listOf(
                QuizQuestion(
                    id = "senior_se_mod3_q1",
                    question = "Apa maksud dari ‘eventual consistency’?",
                    options = listOf(
                        "Data harus selalu konsisten setiap saat",
                        "Data akan konsisten setelah beberapa waktu",
                        "Replikasi data tidak diperlukan",
                        "Transaksi selalu bersifat strongly consistent"
                    ),
                    correctOptionIndex = 1
                )
            ),
            status = RoadmapModuleStatus.LOCKED,
            updatedAt = "2024-11-27 10:18:40.456Z"
        )

        return RoadmapRole(
            id = "senior_software_engineer",
            name = "Senior Software Engineer",
            progress = 0f,
            modules = listOf(module1, module2, module3),
            availableRoles = defaultRoleList(),
            updatedAt = "2024-11-27 10:19:00.123Z"
        )
    }

    // ---------- Role 2: Frontend Developer ----------
    private fun createFrontendDeveloper(): RoadmapRole {

        val module1 = RoadmapModule(
            id = "fe_dev_mod1",
            number = 1,
            title = "HTML, CSS, & Responsive Design",
            description = "Fundamental teknologi frontend dan bagaimana membuat layout responsif.",
            progress = 0f,
            color = BluePrimary,
            learningPoints = listOf(
                "Semantic HTML",
                "Flexbox & Grid",
                "Responsive Layout",
                "Basic Accessibility"
            ),
            articles = listOf(
                RoadmapArticle(
                    id = "fe_dev_mod1_art1",
                    title = "Introduction to Semantic HTML",
                    content = "Penggunaan tag HTML semantik untuk meningkatkan struktur dan aksesibilitas.",
                    order = 1,
                    updatedAt = "2024-11-27 10:20:10.123Z"
                ),
                RoadmapArticle(
                    id = "fe_dev_mod1_art2",
                    title = "Responsive Web Design",
                    content = "Media query, fluid layout, dan teknik modern responsive design.",
                    order = 2,
                    updatedAt = "2024-11-27 10:20:30.456Z"
                ),
                RoadmapArticle(
                    id = "fe_dev_mod1_art3",
                    title = "CSS Flexbox & Grid",
                    content = "Dasar-dasar Flexbox dan CSS Grid untuk menyusun layout kompleks.",
                    order = 3,
                    updatedAt = "2024-11-27 10:20:50.789Z"
                )
            ),
            quizTitle = "Kuis Dasar Frontend",
            questions = listOf(
                QuizQuestion(
                    id = "fe_dev_mod1_q1",
                    question = "Tag HTML mana yang paling tepat untuk judul utama halaman?",
                    options = listOf("div", "h1", "span", "section"),
                    correctOptionIndex = 1
                ),
                QuizQuestion(
                    id = "fe_dev_mod1_q2",
                    question = "Teknik apa yang biasa digunakan untuk membuat layout responsif?",
                    options = listOf(
                        "Fixed width layout",
                        "Table-based layout",
                        "Media query dan fluid layout",
                        "Inline style di setiap elemen"
                    ),
                    correctOptionIndex = 2
                )
            ),
            status = RoadmapModuleStatus.LOCKED,
            updatedAt = "2024-11-27 10:21:10.123Z"
        )

        val module2 = RoadmapModule(
            id = "fe_dev_mod2",
            number = 2,
            title = "JavaScript & TypeScript",
            description = "Fundamental JavaScript modern dan pengenalan TypeScript untuk proyek skala besar.",
            progress = 0f,
            color = OrangePrimary,
            learningPoints = listOf(
                "ES6+ Features",
                "Asynchronous Programming",
                "Module System",
                "Basic TypeScript"
            ),
            articles = listOf(
                RoadmapArticle(
                    id = "fe_dev_mod2_art1",
                    title = "Modern JavaScript (ES6+)",
                    content = "let/const, arrow function, spread operator, dan fitur modern lainnya.",
                    order = 1,
                    updatedAt = "2024-11-27 10:21:30.123Z"
                ),
                RoadmapArticle(
                    id = "fe_dev_mod2_art2",
                    title = "Promises & Async/Await",
                    content = "Cara menangani operasi asynchronous dengan Promise dan async/await.",
                    order = 2,
                    updatedAt = "2024-11-27 10:21:50.456Z"
                ),
                RoadmapArticle(
                    id = "fe_dev_mod2_art3",
                    title = "Introduction to TypeScript",
                    content = "Typing dasar, interface, dan manfaat TypeScript di codebase besar.",
                    order = 3,
                    updatedAt = "2024-11-27 10:22:10.789Z"
                )
            ),
            quizTitle = "Kuis JavaScript & TypeScript",
            questions = listOf(
                QuizQuestion(
                    id = "fe_dev_mod2_q1",
                    question = "Keyword mana yang bersifat block-scoped pada JavaScript?",
                    options = listOf("var", "let", "function", "this"),
                    correctOptionIndex = 1
                )
            ),
            status = RoadmapModuleStatus.LOCKED,
            updatedAt = "2024-11-27 10:22:30.123Z"
        )

        val module3 = RoadmapModule(
            id = "fe_dev_mod3",
            number = 3,
            title = "Frontend Framework (React/Vue)",
            description = "Membangun aplikasi web modern menggunakan library/framework populer.",
            progress = 0f,
            color = BluePrimary,
            learningPoints = listOf(
                "Component-Based Architecture",
                "State Management",
                "Routing",
                "Hooks / Composition API"
            ),
            articles = listOf(
                RoadmapArticle(
                    id = "fe_dev_mod3_art1",
                    title = "Getting Started with React",
                    content = "Membangun komponen, props, dan state di React.",
                    order = 1,
                    updatedAt = "2024-11-27 10:22:50.456Z"
                ),
                RoadmapArticle(
                    id = "fe_dev_mod3_art2",
                    title = "Vue Composition API Overview",
                    content = "Pendekatan Composition API untuk struktur kode yang lebih modular.",
                    order = 2,
                    updatedAt = "2024-11-27 10:23:10.789Z"
                )
            ),
            quizTitle = "Kuis Framework",
            questions = listOf(
                QuizQuestion(
                    id = "fe_dev_mod3_q1",
                    question = "Apa kelebihan utama React dibanding pendekatan tradisional?",
                    options = listOf(
                        "Template berbasis string",
                        "Component-Based UI",
                        "Two-way binding bawaan",
                        "Tidak membutuhkan build tools"
                    ),
                    correctOptionIndex = 1
                )
            ),
            status = RoadmapModuleStatus.LOCKED,
            updatedAt = "2024-11-27 10:23:30.123Z"
        )

        return RoadmapRole(
            id = "frontend_developer",
            name = "Frontend Developer",
            progress = 0f,
            modules = listOf(module1, module2, module3),
            availableRoles = defaultRoleList(),
            updatedAt = "2024-11-27 10:23:50.456Z"
        )
    }

    // ---------- Role 3: Machine Learning Engineer ----------
    private fun createMachineLearningEngineer(): RoadmapRole {

        val module1 = RoadmapModule(
            id = "ml_eng_mod1",
            number = 1,
            title = "Python for Data & ML",
            description = "Dasar Python untuk analisis data dan eksperimen machine learning.",
            progress = 0f,
            color = BluePrimary,
            learningPoints = listOf(
                "Numpy & Pandas",
                "Data Cleaning",
                "Visualization dasar",
                "Environment & Notebook"
            ),
            articles = listOf(
                RoadmapArticle(
                    id = "ml_eng_mod1_art1",
                    title = "Pandas for Data Analysis",
                    content = "DataFrame, selection, agregasi, dan operasi dasar lainnya.",
                    order = 1,
                    updatedAt = "2024-11-27 10:24:10.123Z"
                ),
                RoadmapArticle(
                    id = "ml_eng_mod1_art2",
                    title = "Matplotlib & Seaborn Overview",
                    content = "Visualisasi dasar untuk eksplorasi data.",
                    order = 2,
                    updatedAt = "2024-11-27 10:24:30.456Z"
                )
            ),
            quizTitle = "Kuis Python & Data",
            questions = listOf(
                QuizQuestion(
                    id = "ml_eng_mod1_q1",
                    question = "Library apa yang umum digunakan untuk manipulasi data tabular di Python?",
                    options = listOf("NumPy", "Pandas", "Matplotlib", "TensorFlow"),
                    correctOptionIndex = 1
                )
            ),
            status = RoadmapModuleStatus.LOCKED,
            updatedAt = "2024-11-27 10:24:50.789Z"
        )

        val module2 = RoadmapModule(
            id = "ml_eng_mod2",
            number = 2,
            title = "Supervised Learning",
            description = "Konsep supervised learning dan algoritma klasik (regresi, klasifikasi).",
            progress = 0f,
            color = OrangePrimary,
            learningPoints = listOf(
                "Train / Validation / Test Split",
                "Linear & Logistic Regression",
                "Tree-based Models",
                "Evaluation Metrics"
            ),
            articles = listOf(
                RoadmapArticle(
                    id = "ml_eng_mod2_art1",
                    title = "Train, Validation, Test Split",
                    content = "Mengapa membagi data penting untuk menghindari overfitting.",
                    order = 1,
                    updatedAt = "2024-11-27 10:25:10.123Z"
                ),
                RoadmapArticle(
                    id = "ml_eng_mod2_art2",
                    title = "Evaluation Metrics for Classification",
                    content = "Precision, recall, F1-score, ROC-AUC, dan kapan menggunakannya.",
                    order = 2,
                    updatedAt = "2024-11-27 10:25:30.456Z"
                )
            ),
            quizTitle = "Kuis Supervised Learning",
            questions = listOf(
                QuizQuestion(
                    id = "ml_eng_mod2_q1",
                    question = "Metric mana yang paling tepat ketika dataset sangat imbalance?",
                    options = listOf("Accuracy", "MSE", "Precision/Recall", "R-squared"),
                    correctOptionIndex = 2
                )
            ),
            status = RoadmapModuleStatus.LOCKED,
            updatedAt = "2024-11-27 10:25:50.789Z"
        )

        val module3 = RoadmapModule(
            id = "ml_eng_mod3",
            number = 3,
            title = "Machine Learning in Production",
            description = "MLOps dasar, deployment model, dan monitoring performa di production.",
            progress = 0f,
            color = BluePrimary,
            learningPoints = listOf(
                "Model Serving",
                "Batch vs Online Inference",
                "Monitoring & Drift",
                "Experiment Tracking"
            ),
            articles = listOf(
                RoadmapArticle(
                    id = "ml_eng_mod3_art1",
                    title = "Introduction to MLOps",
                    content = "Prinsip umum MLOps dan lifecycle model machine learning.",
                    order = 1,
                    updatedAt = "2024-11-27 10:26:10.123Z"
                )
            ),
            quizTitle = "Kuis ML Production",
            questions = listOf(
                QuizQuestion(
                    id = "ml_eng_mod3_q1",
                    question = "Apa tujuan utama monitoring model di production?",
                    options = listOf(
                        "Mengurangi ukuran file model",
                        "Memastikan performa model tetap stabil seiring waktu",
                        "Mempercepat proses training",
                        "Mengurangi jumlah feature"
                    ),
                    correctOptionIndex = 1
                )
            ),
            status = RoadmapModuleStatus.LOCKED,
            updatedAt = "2024-11-27 10:26:30.456Z"
        )

        return RoadmapRole(
            id = "machine_learning_engineer",
            name = "Machine Learning Engineer",
            progress = 0f,
            modules = listOf(module1, module2, module3),
            availableRoles = defaultRoleList(),
            updatedAt = "2024-11-27 10:26:50.789Z"
        )
    }

    // ---------- Helper ----------
    private fun defaultRoleList(): List<String> = listOf(
        "Senior Software Engineer",
        "Frontend Developer",
        "Machine Learning Engineer"
    )
}
