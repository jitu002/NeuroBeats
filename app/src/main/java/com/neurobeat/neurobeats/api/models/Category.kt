package com.neurobeat.neurobeats.api.models


data class CategoriesResponse(val categories: Categories)
data class Categories(val items: List<Category>)
data class Category(val id: String, val name: String)