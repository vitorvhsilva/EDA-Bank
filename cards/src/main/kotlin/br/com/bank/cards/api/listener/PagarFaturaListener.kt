package br.com.bank.cards.api.listener

import br.com.bank.cards.api.dto.events.PagarFaturaEventDTO
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.FaturaRepository
import br.com.bank.cards.domain.utils.enums.StatusFatura
import br.com.bank.cards.domain.utils.enums.StatusTransacao
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class PagarFaturaListener (
    private val logger: Logger,
    private val faturaRepository: FaturaRepository,
    private val cartaoRepository: CartaoRepository,
    private val respostaFaturaKafkaTemplate: KafkaTemplate<String, PagarFaturaEventDTO>
) {
    @Transactional
    @KafkaListener(topics = ["pagar-fatura-topic"], groupId = "pagar-fatura-consumer",
        containerFactory = "pagarFaturaContainerFactory")
    fun processarPedido(event: PagarFaturaEventDTO) {
        logger.info("Pedido de pagar fatura do usuário ${event.idUsuario} para o mês ${event.mesAnoFatura} recebido!")

        val fatura = faturaRepository.findByMesAnoFatura(event.mesAnoFatura)

        if (fatura.isEmpty) {
            logger.error("A fatura desse mês não existe!")
            event.status = StatusTransacao.INVALIDA
            event.mensagem = "A fatura desse mês não existe!"

            respostaFaturaKafkaTemplate.send("resposta-fatura-topic", event.idUsuario, event)

            return
        }

        val faturaPega = fatura.get()

        if (!faturaPega.valorFatura.equals(event.valorFatura)) {
            logger.error("Valor da fatura diferente do que deveria!")
            event.status = StatusTransacao.INVALIDA
            event.mensagem = "Valor da fatura diferente do que deveria!"

            respostaFaturaKafkaTemplate.send("resposta-fatura-topic", event.idUsuario, event)

            return
        }

        if (faturaPega.status.equals(StatusFatura.PAGA)) {
            logger.error("Fatura já foi paga!")
            event.status = StatusTransacao.INVALIDA
            event.mensagem = "Fatura já foi paga!"

            respostaFaturaKafkaTemplate.send("resposta-fatura-topic", event.idUsuario, event)

            return
        }

        faturaPega.status = StatusFatura.PAGA
        event.status = StatusTransacao.VALIDA
        event.mensagem = "Fatura processada com sucesso!"

        respostaFaturaKafkaTemplate.send("resposta-fatura-topic", event.idUsuario, event)

        val cartao = cartaoRepository.findById(event.idCartao).get()
        cartao.limite += faturaPega.valorFatura

        logger.info("Pedido de pagar fatura do usuário ${event.idUsuario} para o mês ${event.mesAnoFatura} processado!")
    }

}