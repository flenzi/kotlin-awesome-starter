package com.example.company.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
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
            .withAllAnnotationsOf(
                org.springframework.web.bind.annotation.RestController::class,
                org.springframework.stereotype.Controller::class
            )
            .assertTrue {
                it.name.endsWith("Controller")
            }
    }

    @Test
    fun `classes with 'Service' annotation should have 'Service' suffix`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withAllAnnotationsOf(org.springframework.stereotype.Service::class)
            .assertTrue {
                it.name.endsWith("Service")
            }
    }

    @Test
    fun `classes with 'Repository' annotation should have 'Repository' suffix`() {
        Konsist
            .scopeFromProject()
            .interfaces()
            .withAllAnnotationsOf(org.springframework.stereotype.Repository::class)
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
            .packages
            .filter { it.name.startsWith("com.example.company.domain") }
            .assertTrue { pkg ->
                val domainName = pkg.name.split(".").getOrNull(4)
                if (domainName != null) {
                    pkg.files.all { file ->
                        file.imports.none { import ->
                            import.name.contains("com.example.company.domain") &&
                                !import.name.contains(domainName)
                        }
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
            .withAllAnnotationsOf(jakarta.persistence.Entity::class)
            .assertTrue {
                it.resideInPackage("..model..")
            }
    }
}
