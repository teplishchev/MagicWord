package com.example.magicwordsgameteplishchev.models

enum class GameMode(description: String) {
    FOUR_LETTERS_SIMPLE("simple_4"),
    FIVE_LETTERS_SIMPLE("simple_5"),
    THREE_LETTERS_SIMPLE("simple_3"),
    FOUR_LETTERS_HARD("hard_4"),
    FIVE_LETTERS_HARD("hard_5"),
    THREE_LETTERS_HARD("hard_3"),
    LIMIT_ATTEMPTS_FOUR_SIMPLE("limit_att_simple_4"),
    LIMIT_ATTEMPTS_FIVE_SIMPLE("limit_att_simple_5"),
    LIMIT_ATTEMPTS_THREE_SIMPLE("limit_att_simple_3")
}