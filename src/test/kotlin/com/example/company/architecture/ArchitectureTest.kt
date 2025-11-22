package com.example.company.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withAnnotationOf
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

/**
 * Architecture tests using Konsist to enforce domain-driven design principles.
 *
 * These tests ensure:
 * - Proper package structure (controllers, services, repositories within domains)
 * - Correct naming conventions
 * - Layer dependencies (controller -> service -> repository)
 * - No circular dependencies between domains
 */
class ArchitectureTest {

    @Test
    fun `controllers should reside in controller package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("Controller")
            .assertTrue { it.resideInPackage("..controller..") }
    }

    @Test
    fun `services should reside in service package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("Service")
            .assertTrue { it.resideInPackage("..service..") }
    }

    @Test
    fun `repositories should reside in repository package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("Repository")
            .assertTrue { it.resideInPackage("..repository..") }
    }

    @Test
    fun `classes with 'Controller' annotation should have 'Controller' suffix`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withAnnotationOf(org.springframework.web.bind.annotation.RestController::class)
            .assertTrue {
                it.name.endsWith("Controller")
            }
    }

    @Test
    fun `classes with 'Service' annotation should have 'Service' suffix`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withAnnotationOf(org.springframework.stereotype.Service::class)
            .assertTrue {
                it.name.endsWith("Service")
            }
    }

    @Test
    fun `classes with 'Repository' annotation should have 'Repository' suffix`() {
        Konsist
            .scopeFromProject()
            .interfaces()
            .withAnnotationOf(org.springframework.stereotype.Repository::class)
            .assertTrue {
                it.name.endsWith("Repository")
            }
    }

    @Test
    fun `domain layer architecture is respected`() {
        Konsist
            .scopeFromProject()
            .assertArchitecture {
                val controller = Layer("Controller", "..controller..")
                val service = Layer("Service", "..service..")
                val repository = Layer("Repository", "..repository..")
                val model = Layer("Model", "..model..")

                controller.dependsOn(service, model)
                service.dependsOn(repository, model)
                repository.dependsOn(model)
                model.dependsOnNothing()
            }
    }

    @Test
    fun `no circular dependencies between domains`() {
        Konsist
            .scopeFromProject()
            .files
            .filter { it.packagee?.name?.startsWith("com.example.company.domain") == true }
            .assertTrue { file ->
                val domainName = file.packagee?.name?.split(".")?.getOrNull(4)
                if (domainName != null) {
                    file.imports.none { import ->
                        import.name.contains("com.example.company.domain") &&
                            !import.name.contains(domainName)
                    }
                } else {
                    true
                }
            }
    }

    @Test
    fun `controllers should not depend on repositories directly`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("Controller")
            .assertTrue { controller ->
                controller.properties().none { property ->
                    property.type?.name?.endsWith("Repository") == true
                }
            }
    }

    @Test
    fun `entity classes should reside in model package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withAnnotationOf(jakarta.persistence.Entity::class)
            .assertTrue {
                it.resideInPackage("..model..")
            }
    }

    @Test
    fun `no wildcard imports are used`() {
        Konsist
            .scopeFromProject()
            .files
            .assertTrue { file ->
                file.imports.none { import ->
                    import.isWildcard
                }
            }
    }

    @Test
    fun `DTOs should not have JPA annotations`() {
        Konsist
            .scopeFromProject()
            .classes()
            .filter { it.name.endsWith("Request") || it.name.endsWith("Response") }
            .assertTrue { dto ->
                dto.annotations.none { annotation ->
                    annotation.name.startsWith("Entity") ||
                    annotation.name.startsWith("Table") ||
                    annotation.name.startsWith("Column")
                }
            }
    }

    @Test
    fun `services should not depend on controllers`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("Service")
            .assertTrue { service ->
                service.properties().none { property ->
                    property.type?.name?.endsWith("Controller") == true
                }
            }
    }

    @Test
    fun `data classes should have meaningful names`() {
        Konsist
            .scopeFromProject()
            .classes()
            .filter { it.hasModifier("data") }
            .assertTrue { dataClass ->
                dataClass.name.length >= 3 &&
                !dataClass.name.matches(Regex(".*[0-9]$")) // No numbers at end
            }
    }
}
