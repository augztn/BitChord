package com.music.bitchord.data.sources

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * Portable backup of user-configured source links only.
 *
 * Built-in sources are not exported because they are recreated by the app.
 * URLs are included as entered; users should review links before sharing a
 * backup because some services may encode credentials in URL query strings.
 */
object SourceLinksBackup {
    private const val APP_TAG = "bitchord-source-links"
    private const val SCHEMA_VERSION = 1

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
    }

    fun exportJson(): String {
        val userSources = SourceRegistry.configs.value.filter { it.kind.isUserAdded }
        return json.encodeToString(
            BackupFile.serializer(),
            BackupFile(sources = userSources),
        )
    }

    /**
     * Validate the entire file before changing registry state. Only user-added
     * source kinds can be imported; built-in sources remain managed by BitChord.
     */
    fun importJson(text: String): Int {
        require(text.length <= MAX_BACKUP_CHARS) { "Backup file is too large" }
        val backup = json.decodeFromString(BackupFile.serializer(), text)
        require(backup.app == APP_TAG) { "This is not a BitChord source-links backup" }
        require(backup.version <= SCHEMA_VERSION) { "This backup was created by a newer BitChord version" }
        require(backup.sources.size <= MAX_SOURCES) { "Backup contains too many sources" }
        require(backup.sources.all { it.kind.isUserAdded }) {
            "Backup contains a built-in or unsupported source type"
        }
        require(backup.sources.all { it.baseUrl.isNotBlank() && it.baseUrl.length <= MAX_URL_LENGTH }) {
            "Backup contains an empty or invalid source URL"
        }
        require(backup.sources.map { it.id }.distinct().size == backup.sources.size) {
            "Backup contains duplicate source IDs"
        }
        require(backup.sources.map { it.baseUrl.trimEnd('/') }.distinct().size == backup.sources.size) {
            "Backup contains duplicate source URLs"
        }
        SourceRegistry.replaceUserAddedSources(backup.sources)
        return backup.sources.size
    }

    @Serializable
    private data class BackupFile(
        val app: String = APP_TAG,
        val version: Int = SCHEMA_VERSION,
        val sources: List<SourceConfig> = emptyList(),
    )

    private const val MAX_BACKUP_CHARS = 1_000_000
    private const val MAX_SOURCES = 100
    private const val MAX_URL_LENGTH = 4096
}
