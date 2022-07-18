package com.example.template

import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.Behaviors
import akka.management.cluster.bootstrap.ClusterBootstrap
import akka.management.javadsl.AkkaManagement
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.support.GenericApplicationContext

@SpringBootApplication
class SpringAkkaTemplateApplication(
    val applicationContext: ApplicationContext
    ): ApplicationContextInitializer<GenericApplicationContext> {

    @Value("\${akka.service.name}")
    lateinit var actorSystemName: String
    @Bean
    fun actorSystem(): ActorSystem<Void> {
        val system = ActorSystem.create(Behaviors.empty<Void>(), actorSystemName)
        AkkaManagement.get(system).start()
        ClusterBootstrap.get(system).start()
        return system
    }
    override fun initialize(applicationContext: GenericApplicationContext) {
    }

}

fun main(args: Array<String>) {
    runApplication<SpringAkkaTemplateApplication>(*args)
}
