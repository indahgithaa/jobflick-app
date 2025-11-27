package com.example.jobflick.features.jobseeker.roadmap.data.remote

import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.features.jobseeker.roadmap.domain.model.QuizQuestion
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModule
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole

class RoadmapRemoteDataSource {

    fun getRoadmapRole(roleName: String): RoadmapRole {
        return when (roleName) {

            // ============================================================
            // 1. SENIOR SOFTWARE ENGINEER
            // ============================================================
            "Senior Software Engineer" -> createSeniorSoftwareEngineer()

            // ============================================================
            // 2. FRONTEND DEVELOPER
            // ============================================================
            "Frontend Developer" -> createFrontendDeveloper()

            // ============================================================
            // 3. MACHINE LEARNING ENGINEER
            // ============================================================
            "Machine Learning Engineer" -> createMachineLearningEngineer()

            // Default: fallback ke Senior Software Engineer
            else -> createSeniorSoftwareEngineer()
        }
    }

    // ===================================================================
    // ROLE 1 — SENIOR SOFTWARE ENGINEER
    // ===================================================================
    private fun createSeniorSoftwareEngineer(): RoadmapRole {

        val module1 = RoadmapModule(
            number = 1,
            title = "System Design & Architecture",
            description = "Mempelajari arsitektur sistem skala besar, microservices, CAP theorem, distributed caching, dan scalability.",
            progress = 0.25f,
            color = BluePrimary,
            learningPoints = listOf(
                "Scalability dan reliability",
                "Microservices vs monolith",
                "Event-driven architecture",
                "CAP theorem & data consistency"
            ),
            articles = listOf(
                "Introduction to System Architecture",
                "High Availability & Fault Tolerance",
                "Caching Strategies for High Traffic"
            ),
            quizTitle = "Kuis System Design",
            questions = listOf(
                QuizQuestion(
                    "Apa tujuan utama dari caching?",
                    listOf(
                        "Mengurangi biaya server",
                        "Meningkatkan latency & throughput",
                        "Mempermudah debugging",
                        "Mengurangi load testing"
                    )
                ),
                QuizQuestion(
                    "Apa makna ‘Partition Tolerance’ di CAP theorem?",
                    listOf(
                        "Sistem tetap berjalan meskipun ada network failure",
                        "Database tidak membutuhkan sharding",
                        "Sistem tidak akan memiliki downtime",
                        "Tidak perlu menggunakan load balancer"
                    )
                )
            )
        )

        val module2 = RoadmapModule(
            number = 2,
            title = "Software Engineering Best Practices",
            description = "Best practices dalam menulis kode berkualitas tinggi, maintainable, serta efisiensi engineering workflow.",
            progress = 0f,
            color = OrangePrimary,
            learningPoints = listOf(
                "Clean Code & Clean Architecture",
                "Code Review Best Practices",
                "CI/CD Pipeline",
                "Refactoring Techniques"
            ),
            articles = listOf(
                "Clean Code Principles",
                "Introduction to CI/CD",
                "Effective Code Reviews",
                "Refactoring Techniques",
                "Documentation Best Practices"
            ),
            quizTitle = "Kuis Best Practices",
            questions = listOf(
                QuizQuestion(
                    "Apa tujuan utama dari code review?",
                    listOf(
                        "Mencari kesalahan kecil",
                        "Meningkatkan kualitas kode dan knowledge sharing",
                        "Menambah waktu pengerjaan",
                        "Membuat proses debugging lebih lambat"
                    )
                )
            )
        )

        val module3 = RoadmapModule(
            number = 3,
            title = "Distributed Systems",
            description = "Konsep distributed systems, message queue, load balancing, service discovery, serta event-driven design.",
            progress = 0f,
            color = BluePrimary,
            learningPoints = listOf(
                "Message Queue (Kafka, RabbitMQ)",
                "Load Balancing Techniques",
                "Service Discovery",
                "Eventual Consistency"
            ),
            articles = listOf(
                "Introduction to Distributed Systems",
                "Message Queue Explained",
                "Service Discovery in Microservices"
            ),
            quizTitle = "Kuis Distributed Systems",
            questions = listOf(
                QuizQuestion(
                    "Apa itu ‘eventual consistency’?",
                    listOf(
                        "Data harus konsisten setiap saat",
                        "Konsistensi dijamin setelah beberapa waktu",
                        "Database tidak perlu replikasi",
                        "Load balancer tidak diperlukan"
                    )
                )
            )
        )

        return RoadmapRole(
            name = "Senior Software Engineer",
            progress = 0.25f,
            modules = listOf(module1, module2, module3),
            availableRoles = defaultRoleList()
        )
    }


    // ===================================================================
    // ROLE 2 — FRONTEND DEVELOPER
    // ===================================================================
    private fun createFrontendDeveloper(): RoadmapRole {

        val module1 = RoadmapModule(
            number = 1,
            title = "HTML, CSS, & Responsive Design",
            description = "Fundamental teknologi frontend dan membuat UI responsif.",
            progress = 0.3f,
            color = BluePrimary,
            learningPoints = listOf(
                "Semantic HTML",
                "Flexbox & Grid",
                "Responsive Layout",
                "Accessibility"
            ),
            articles = listOf(
                "Introduction to HTML Semantic",
                "Responsive Web Design Principles",
                "Mastering Flexbox"
            ),
            quizTitle = "Kuis Dasar HTML/CSS",
            questions = listOf(
                QuizQuestion(
                    "Apa tujuan dari semantic HTML?",
                    listOf("Agar website lebih cepat", "Meningkatkan aksesibilitas", "Mempermudah styling", "Mengurangi ukuran file")
                )
            )
        )

        val module2 = RoadmapModule(
            number = 2,
            title = "JavaScript & ES6+",
            description = "Dasar JavaScript modern untuk membangun aplikasi interaktif.",
            progress = 0f,
            color = OrangePrimary,
            learningPoints = listOf(
                "Variable & Scope",
                "Arrow Function",
                "Promise & Async Await",
                "DOM Manipulation"
            ),
            articles = listOf(
                "JavaScript Fundamentals",
                "Understanding Async/Await",
                "Modern ES6 Features"
            ),
            quizTitle = "Kuis JavaScript",
            questions = listOf(
                QuizQuestion(
                    "Apa fungsi dari async/await?",
                    listOf("Membuat gaya penulisan asynchronous lebih mirip synchronous", "Meningkatkan performa browser", "Menghapus callback", "Menghindari DOM rerender")
                )
            )
        )

        val module3 = RoadmapModule(
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
                "Getting Started with React",
                "Vue Composition API Overview",
                "Understanding State Management"
            ),
            quizTitle = "Kuis Framework",
            questions = listOf(
                QuizQuestion(
                    "Apa kelebihan utama React?",
                    listOf("Template System", "Component-Based UI", "Two-Way Binding default", "Tidak membutuhkan build tool")
                )
            )
        )

        return RoadmapRole(
            name = "Frontend Developer",
            progress = 0.3f,
            modules = listOf(module1, module2, module3),
            availableRoles = defaultRoleList()
        )
    }


    // ===================================================================
    // ROLE 3 — MACHINE LEARNING ENGINEER
    // ===================================================================
    private fun createMachineLearningEngineer(): RoadmapRole {

        val module1 = RoadmapModule(
            number = 1,
            title = "Python & Data Processing",
            description = "Fundamental Python, NumPy, Pandas untuk data manipulation.",
            progress = 0.1f,
            color = BluePrimary,
            learningPoints = listOf(
                "NumPy Arrays",
                "Pandas DataFrame",
                "Data Cleaning",
                "Exploratory Data Analysis"
            ),
            articles = listOf(
                "Python for Data Science",
                "Working with Pandas",
                "Data Cleaning Techniques"
            ),
            quizTitle = "Kuis Data Processing",
            questions = listOf(
                QuizQuestion(
                    "Apa fungsi utama Pandas DataFrame?",
                    listOf("Menyimpan file CSV", "Struktur data tabular untuk analisis", "Menjalankan neural network", "Melakukan inference model")
                )
            )
        )


        val module2 = RoadmapModule(
            number = 2,
            title = "Machine Learning Fundamentals",
            description = "Dasar ML, supervised learning, evaluation metrics, overfitting.",
            progress = 0f,
            color = OrangePrimary,
            learningPoints = listOf(
                "Linear Regression",
                "Classification Models",
                "Overfitting vs Underfitting",
                "Evaluation Metrics"
            ),
            articles = listOf(
                "Introduction to Machine Learning",
                "Bias-Variance Tradeoff",
                "ML Evaluation Metrics"
            ),
            quizTitle = "Kuis Dasar ML",
            questions = listOf(
                QuizQuestion(
                    "Apa tujuan dari regularisasi?",
                    listOf("Meningkatkan ukuran dataset", "Mengurangi overfitting", "Menambah layer neural network", "Mempercepat training")
                )
            )
        )

        val module3 = RoadmapModule(
            number = 3,
            title = "Deep Learning & Neural Networks",
            description = "Konsep deep learning, arsitektur neural network, training pipeline.",
            progress = 0f,
            color = BluePrimary,
            learningPoints = listOf(
                "Neural Network Architecture",
                "Loss Function",
                "Backpropagation",
                "Optimization (SGD, Adam)"
            ),
            articles = listOf(
                "Intro to Neural Networks",
                "Understanding Backpropagation",
                "Activation Functions"
            ),
            quizTitle = "Kuis Deep Learning",
            questions = listOf(
                QuizQuestion(
                    "Apa fungsi activation function?",
                    listOf("Menentukan ukuran model", "Menambah interpretabilitas", "Membuat model dapat mempelajari non-linearitas", "Menghentikan training")
                )
            )
        )

        return RoadmapRole(
            name = "Machine Learning Engineer",
            progress = 0.1f,
            modules = listOf(module1, module2, module3),
            availableRoles = defaultRoleList()
        )
    }


    // ===================================================================
    // DEFAULT ROLE LIST (dipakai di halaman “Pilih Role”)
    // ===================================================================
    private fun defaultRoleList(): List<String> {
        return listOf(
            "Senior Software Engineer",
            "Frontend Developer",
            "Machine Learning Engineer"
        )
    }
}
