package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.ItemPedido;
import com.cursospring.cursomc.domain.PagamentoComBoleto;
import com.cursospring.cursomc.domain.Pedido;
import com.cursospring.cursomc.domain.enums.EstadoPagamento;
import com.cursospring.cursomc.repositories.ItemPedidoRepository;
import com.cursospring.cursomc.repositories.PagamentoRepository;
import com.cursospring.cursomc.repositories.PedidoRepository;
import com.cursospring.cursomc.services.exceptions.AuthorizationException;
import com.cursospring.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmailService emailService;

    public Pedido findById(Integer id) throws ObjectNotFoundException {
        Optional<Pedido> obj = pedidoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id +
                ", Tipo: " + Pedido.class.getName()));
    }

    @Transactional
    public Pedido insert(Pedido obj) throws ObjectNotFoundException, AuthorizationException {
        obj.setId(null);
        obj.setInstante(new Date());
        obj.setCliente(clienteService.findById(obj.getCliente().getId()));
        obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);

        if (obj.getPagamento() instanceof PagamentoComBoleto){
            PagamentoComBoleto pagamentoComCartao = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preencherPagamentoComBoleto(pagamentoComCartao, obj.getInstante());
        }
        obj = pedidoRepository.save(obj);
        pagamentoRepository.save(obj.getPagamento());

        for (ItemPedido itemPedido : obj.getItens()) {
            itemPedido.setDesconto(0.0);
            itemPedido.setProduto(produtoService.findById(itemPedido.getProduto().getId()));
            itemPedido.setPreco(itemPedido.getProduto().getPreco());
            itemPedido.setPedido(obj);
        }

        itemPedidoRepository.saveAll(obj.getItens());
        //System.out.println(obj);
        //emailService.sendOrderConfirmationEmail(obj);
        emailService.sendOrderConfirmationHtmlEmail(obj);
        return obj;
    }

    //produtoSerice


}
