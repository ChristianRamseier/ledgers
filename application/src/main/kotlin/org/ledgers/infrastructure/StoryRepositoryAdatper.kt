package org.ledgers.infrastructure

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.ledgers.domain.Story
import org.ledgers.domain.StoryId
import org.ledgers.domain.repository.StoryRepository
import org.ledgers.dto.StoryDto
import org.ledgers.dto.toDomain
import org.ledgers.dto.toDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class StoryRepositoryAdapter(
    @Value("\${org.ledger.storage.path}")
    private val storagePath: String,
) : StoryRepository {


    override fun save(story: Story): Story {
        val storyDto = story.toDto()
        val json = Json.encodeToString(storyDto)
        val file = File("$storagePath/${Filename(storyDto.id)}")
        file.writeText(json)
        return readStory(file)
    }

    override fun findAll(): List<Story> {
        return File(storagePath)
            .listFiles { _, name -> Filename.isValid(name) }
            .map { file -> readStory(file) }
    }

    private fun readStory(file: File): Story {
        val storyDto = Json.decodeFromStream<StoryDto>(file.inputStream())
        return storyDto.toDomain()
    }

    override fun findById(storyId: StoryId): Story? {
        val file = File("$storagePath/${Filename(storyId)}")
        if(file.exists()) {
            return readStory(file)
        }
        return null
    }

    private class Filename {

        val storyId: StoryId

        constructor(storyId: StoryId) {
            this.storyId = storyId
        }

        constructor(filename: String) {
            if (!isValid(filename)) {
                throw RuntimeException("Invalid filename")
            }
            val matchEntire = pattern.matchEntire(filename)!!
            storyId = StoryId(matchEntire.groupValues[0])
        }

        override fun toString(): String {
            return "${storyId}.json"
        }

        companion object {
            private val pattern = Regex("^[a-f0-9-]+\\.json$")

            /**
             * Is valid when it follows the following pattern:
             * <storyId>.json
             */
            fun isValid(name: String): Boolean {
                return pattern.matches(name)
            }

        }
    }

}
