package com.example.wubbalistdubapp.data

interface CharactersRepository {
    fun getAll(): List<Character>
    fun getById(id: Int): Character?
}

class MockCharactersRepository : CharactersRepository {
    private val items = listOf(
        Character(
            1,"Rick Sanchez","Alive","Human","Male",
            "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            "Earth (C-137)","Citadel of Ricks"
        ),
        Character(
            2,"Morty Smith","Alive","Human","Male",
            "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
            "unknown","Earth (Replacement Dimension)"
        ),
        Character(
            3,"Summer Smith","Alive","Human","Female",
            "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
            "Earth (Replacement Dimension)","Earth (Replacement Dimension)"
        ),
        Character(
            4,"Beth Smith","Alive","Human","Female",
            "https://rickandmortyapi.com/api/character/avatar/4.jpeg",
            "Earth (Replacement Dimension)","Earth (Replacement Dimension)"
        ),
        Character(
            5,"Jerry Smith","Alive","Human","Male",
            "https://rickandmortyapi.com/api/character/avatar/5.jpeg",
            "Earth (Replacement Dimension)","Earth (Replacement Dimension)"
        )
    )
    override fun getAll(): List<Character> = items
    override fun getById(id: Int): Character? = items.firstOrNull { it.id == id }
}


