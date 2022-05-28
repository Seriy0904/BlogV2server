package dev.seriy.blogv2.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users: Table() {
    private val login = Users.varchar("login",25)
    private val username = Users.varchar("login",30)
    private val photourl = Users.varchar("photourl",40)
    fun insert(userDTO: UserDTO){
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[username] = userDTO.username
                it[photourl] = userDTO.photourl
            }
        }
    }
    fun getUser(login:String): UserDTO{
        val userModel = Users.select{Users.login.eq(login)}.single()
        return UserDTO(
            login = userModel[Users.login],
            username = userModel[username],
            photourl = userModel[photourl]
        )
    }
}