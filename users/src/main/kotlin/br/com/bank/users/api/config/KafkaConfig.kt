package br.com.bank.users.api.config

import br.com.bank.users.api.dto.events.PedidoCartaoCompletoDTO
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaConfig (
    @Value(value = "\${spring.kafka.bootstrap-servers:localhost:9092}") private val bootstrapAddress: String
) {
    @Bean
    fun producerFactory(): ProducerFactory<String, PedidoCartaoCompletoDTO> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress)
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer::class.java)
        return DefaultKafkaProducerFactory<String, PedidoCartaoCompletoDTO>(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, PedidoCartaoCompletoDTO> {
        return KafkaTemplate<String, PedidoCartaoCompletoDTO>(producerFactory())
    }
}