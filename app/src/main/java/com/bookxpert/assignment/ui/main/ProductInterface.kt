package com.bookxpert.assignment.ui.main

import com.bookxpert.assignment.database.User

interface ProductInterface {
    fun productEdit(user: User)

    fun productDelete(user: User)
}