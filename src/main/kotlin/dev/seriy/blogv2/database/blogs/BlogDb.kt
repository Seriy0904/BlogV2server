package dev.seriy.blogv2.database.blogs

import dev.seriy.blogv2.features.blogs.models.BlogModel
import dev.seriy.blogv2.features.blogs.models.arrayToString
import dev.seriy.blogv2.features.blogs.models.stringToArray
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object Blogs : Table() {
    private val uuid = Blogs.uuid("uuid")
    private val tittle = Blogs.varchar("tittle", 25)
    private val login = Blogs.varchar("login", 25)
    private val userName = Blogs.varchar("username", 25)
    private val timelong = Blogs.long("timelong")
    private val description = Blogs.text("description")
    private val photosurl = Blogs.text("photosurl")
    fun insert(blogModel: BlogModel) {
        transaction {
            Blogs.insert {
                it[uuid] = blogModel.uuid
                it[tittle] = blogModel.tittle
                it[login] = blogModel.login
                it[userName] = blogModel.userName
                it[timelong] = blogModel.timelong
                it[description] = blogModel.description
                it[photosurl] = arrayToString(blogModel.photosurl)
            }
        }
    }

    fun getAll(): List<BlogModel> {
        return try {
            transaction {
                Blogs.selectAll().toList()
                    .map { blogModel ->
                        BlogModel(
                            uuid = blogModel[uuid],
                            tittle = blogModel[tittle],
                            login = blogModel[login],
                            userName = blogModel[userName],
                            timelong = blogModel[timelong],
                            description = blogModel[description],
                            photosurl = stringToArray(blogModel[photosurl])
                        )
                    }
            }
        } catch (e: NoSuchElementException) {
            emptyList()
        }
    }

    fun getUser(uuid: UUID): BlogModel? {
        return try {
            transaction {
                val blogModel = Blogs.select { Blogs.uuid.eq(uuid) }.single()
                BlogModel(
                    uuid = blogModel[Blogs.uuid],
                    tittle = blogModel[tittle],
                    login = blogModel[login],
                    userName = blogModel[userName],
                    timelong = blogModel[timelong],
                    description = blogModel[description],
                    photosurl = stringToArray(blogModel[photosurl])
                )
            }
        } catch (e: NoSuchElementException) {
            null
        }
    }

    fun edit(
        uuid: UUID,
        tittle: String = "",
        login: String = "",
        userName: String = "",
        description: String = "",
        photosurl: String = ""
    ) {
        transaction {
            Blogs.update {
                it[Blogs.uuid] = uuid
                it[Blogs.photosurl] = photosurl
            }
        }
    }

    fun exists(uuid: UUID) = transaction {
        !Blogs.select { Blogs.uuid.eq(uuid) }.empty()
    }

    fun deleteBlog(uuid: UUID) {
        transaction {
            Blogs.deleteWhere { Blogs.uuid.eq(uuid) }
        }
    }


    fun deleteDirectory(directory: Path?) {
        Files.walk(directory)
            .sorted(Comparator.reverseOrder())
            .map { it.toFile() }
            .forEach { it.delete() }
    }
}