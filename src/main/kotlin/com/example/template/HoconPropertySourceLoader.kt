package com.example.template

import com.typesafe.config.ConfigFactory
import org.springframework.boot.env.PropertySourceLoader
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.Resource

class HoconPropertySourceLoader: PropertySourceLoader {
    override fun getFileExtensions(): Array<String> = arrayOf("conf")

    override fun load(name: String, resource: Resource): List<PropertySource<*>> {
        val config = ConfigFactory.parseURL(resource.url)

        val result = buildFlattenedMap(config.root().unwrapped(), null)

        return if (result.isEmpty()) {
            emptyList()
        } else listOf(MapPropertySource(name, result))
    }

    private fun buildFlattenedMap(source: Map<String, Any?>, root: String?): Map<String, Any?> {
        val rootHasText = null != root && root.trim { it <= ' ' }.isNotEmpty()
        return source.entries.fold(emptyMap()) { acc, (key, value) ->
            val path = if (rootHasText) {
                if (key.startsWith("[")) {
                    root + key
                } else {
                    "$root.$key"
                }
            } else {
                key
            }

            when (value) {
                is Map<*, *> -> acc + buildFlattenedMap(value as Map<String, Any?>, path)
                is Collection<*> -> acc + value.foldIndexed(acc) { index, acc2, obj ->
                    acc2 + buildFlattenedMap(mapOf("[$index]" to obj), path)
                }
                else -> acc + (path to value)
            }
        }
    }
}