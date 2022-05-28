package dev.seriy.blogv2.database.blogs

import dev.seriy.blogv2.features.blogs.models.BlogModel
import dev.seriy.blogv2.features.blogs.models.arrayToString
import dev.seriy.blogv2.features.blogs.models.stringToArray
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object Blogs : Table() {
    private val uuid = uuid("uuid")
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
    fun edit(blog:BlogModel){
        transaction {
            val o = Blogs.select { uuid.eq(uuid) }.single()
            o[tittle] = blog.tittle.ifEmpty { o[tittle] }
            o[login] = blog.login.ifEmpty { o[login] }
            o[userName] = blog.userName.ifEmpty { o[userName] }
            o[description] = blog.description.ifEmpty { o[description] }
            o[photosurl] = blog.photosurl?.ifEmpty { o[photosurl] }
        }
    }
    fun exists(uuid: UUID): Boolean {
        return transaction {
            !Blogs.select { Blogs.uuid.eq(uuid) }.empty()
        }
    }

    fun deleteDirectory(directory: Path?) {
        Files.walk(directory)
            .sorted(Comparator.reverseOrder())
            .map { it.toFile() }
            .forEach { it.delete() }
    }
}