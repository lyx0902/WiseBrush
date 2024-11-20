package com.example.bottomnavigation1.repository

import com.example.bottomnavigation1.database.Database
import com.example.bottomnavigation1.model.User
import com.example.bottomnavigation1.utils.encryptPsw.encryptPassword

import java.sql.SQLException

object UserRepository {

    // 插入用户
    fun addUser(user: User): Boolean {
        val connection = Database.getConnection()
        val sql = "INSERT INTO users (name, password, email) VALUES (?, ?, ?)"
        return try {
            val statement = connection!!.prepareStatement(sql)
            statement.setString(1, user.name)    // 设置 name 参数
            statement.setString(2, encryptPassword(user.password))// 设置 password 参数
            statement.setString(3, user.email)   // 设置 email 参数
            statement.executeUpdate() > 0         // 执行插入操作，成功返回大于0的行数
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        } finally {
            connection?.close()
        }
    }

    // 根据ID查询用户
    fun getUserById(id: Int): User? {
        val connection = Database.getConnection()
        val sql = "SELECT * FROM users WHERE id = ?"
        return try {
            val statement = connection!!.prepareStatement(sql)
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                User(
                    id = resultSet.getInt("id"),
                    name = resultSet.getString("name"),
                    password = resultSet.getString("password"),
                    email = resultSet.getString("email")
                )
            } else {
                null
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        } finally {
            connection?.close()
        }
    }

    //用户名信息
    fun getUserByName(name: String): User? {
        val connection = Database.getConnection()
        val sql = "SELECT * FROM users WHERE name = ?"
        return try {
            val statement = connection!!.prepareStatement(sql)
            statement.setString(1, name)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                User(
                    id = resultSet.getInt("id"),
                    name = resultSet.getString("name"),
                    password = resultSet.getString("password"),
                    email = resultSet.getString("email")
                )
            } else {
                null
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        } finally {
            connection?.close()
        }
    }


    //邮箱
    fun getUserByEmail(email: String): User? {
        val connection = Database.getConnection();
        val sql = "SELECT * FROM users WHERE email = ?";
        return try {
            val statement = connection!!.prepareStatement(sql);
            statement.setString(1, email);
            val resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User(
                    id = resultSet.getInt("id"),
                    name = resultSet.getString("name"),
                    password = resultSet.getString("password"),
                    email = resultSet.getString("email")
                )
            } else {
                null
            }
        } catch (e: SQLException) {
            e.printStackTrace();
            null
        } finally {
            connection?.close();
        }

    }

    // 更新用户信息
    fun updateUser(user: User): Boolean {
        val connection = Database.getConnection()
        val sql = "UPDATE users SET name = ?, password = ?, email = ? WHERE id = ?"
        return try {
            val statement = connection!!.prepareStatement(sql)
            statement.setString(1, user.name)    // 设置 name 参数
            statement.setString(2, encryptPassword(user.password))// 设置 password 参数
            statement.setString(3, user.email)   // 设置 email 参数
            statement.setInt(4, user.id!!)        // 设置 id 参数（用于定位需要更新的记录）
            statement.executeUpdate() > 0         // 执行更新操作，返回影响的行数
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        } finally {
            connection?.close()
        }
    }

    // 删除用户
    fun deleteUser(id: Int): Boolean {
        val connection = Database.getConnection()
        val sql = "DELETE FROM users WHERE id = ?"
        return try {
            val statement = connection!!.prepareStatement(sql)
            statement.setInt(1, id)   // 设置 id 参数
            statement.executeUpdate() > 0   // 执行删除操作，返回影响的行数
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        } finally {
            connection?.close()
        }
    }
}