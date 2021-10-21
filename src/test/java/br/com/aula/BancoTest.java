package br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import br.com.aula.exception.*;
import org.junit.Assert;
import org.junit.Test;

public class BancoTest {

	// A) Cadastrar Contas
	// a) Deve cadastrar uma conta
	// b) Não deve cadastrar conta com número de conta já existente.
	// c) Não deve cadastrar conta com número de conta inválido
	// d) Não deve cadastrar conta com nome de cliente já existente

	// B) Efetuar transferência entre contas
	// a) Deve realizar transferência entre contas Corrente e Poupança
	// b) Deve-se verificar a existência da conta de origem no banco
	// c) Não deve permitir que uma conta de origem do tipo Poupança fique com saldo negativo
	// d) Deve verificar existência da conta de destino no banco
	// e) Não deve permitir transferir um valor negativo

	// A.a) Deve cadastrar uma conta
	@Test
	public void deveCadastrarConta()
			throws ContaJaExistenteException, ContaNumeroInvalidoException, ContaPoupancaSaldoNegativoException {
		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}

	//	A.b) Não deve cadastrar conta com número de conta já existente.
	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroRepetido()
			throws ContaJaExistenteException, ContaNumeroInvalidoException, ContaPoupancaSaldoNegativoException {
		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		// Verificação
		Assert.fail();
	}

	//	A.c) Não deve cadastrar conta com número de conta inválido
	// Solução: Considerar, durante o cadastro, conta inválida caso seu número seja menor que 0
	@Test(expected = ContaNumeroInvalidoException.class)
	public void naoDeveCadastrarContaNumeroInvalido()
			throws ContaJaExistenteException, ContaNumeroInvalidoException, ContaPoupancaSaldoNegativoException {
		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, -5, 0, TipoConta.CORRENTE);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		Assert.fail();
	}

	//	A.d) Não deve cadastrar conta com nome de cliente já existente
	@Test(expected = ContaJaExistenteException.class)
	public void naoCadastrarContaClienteExistente()
			throws ContaNumeroInvalidoException, ContaJaExistenteException, ContaPoupancaSaldoNegativoException {
		// Cenario
		Cliente cliente1 = new Cliente("Joao");
		Conta conta1 = new Conta(cliente1, 0, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Joao");
		Conta conta2 = new Conta(cliente2, 1, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		// Verificação
		Assert.fail();
	}

	// B.a) Deve realizar transferência entre contas Corrente e Poupança
	@Test
	public void deveRealizarTransferenciaEntreContasCorrentePoupanca()
			throws ContaNumeroInvalidoException, ContaJaExistenteException, ContaSemSaldoException,
			ContaNaoExistenteException, ContaPoupancaSaldoNegativoException, BancoTransferenciaSaldoInvalidoException {
		// Cenario
		Cliente cliente1 = new Cliente("Joao");
		Conta conta1 = new Conta(cliente1, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 321, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		banco.efetuarTransferencia(conta1.getNumeroConta(), conta2.getNumeroConta(), 100);

		// Verificação
		assertEquals(conta1.getSaldo(), -100);
		assertEquals(conta2.getSaldo(), 100);
	}

	// B.b) Deve-se verificar a existência da conta de origem no banco
	@Test
	public void deveVerificarExistenciaContaOrigemBanco()
			throws ContaPoupancaSaldoNegativoException, ContaNumeroInvalidoException, ContaJaExistenteException,
			ContaSemSaldoException, ContaNaoExistenteException, BancoTransferenciaSaldoInvalidoException {
		// Cenario
		Cliente cliente1 = new Cliente("Joao");
		Conta conta1 = new Conta(cliente1, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 321, 0, TipoConta.CORRENTE);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		banco.efetuarTransferencia(conta1.getNumeroConta(), conta2.getNumeroConta(), 0);

		// Verificação
		List<Conta> contasCadastradas = banco.obterContas();

		boolean bancoPossuiContaCadastrada1 = contasCadastradas.contains(conta1);
		boolean bancoPossuiContaCadastrada2 = conta1 == banco.obterContaPorNumero(conta1.getNumeroConta());

		assertEquals(true, bancoPossuiContaCadastrada1);
		assertEquals(true, bancoPossuiContaCadastrada2);
	}

	// B.c) Não deve permitir que uma conta de origem do tipo Poupança fique com saldo negativo
	// Solução: Criar nova exceção "ContaPoupancaSaldoNegativoException" e adicioná-la em métodos que possuem "throws"
	// relacionados a conta
	@Test(expected = ContaPoupancaSaldoNegativoException.class)
	public void naoDevePossuirSaldoNegativoEmContaPoupanca() throws
			ContaJaExistenteException, ContaPoupancaSaldoNegativoException, ContaNumeroInvalidoException {
		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);
		conta.debitar(100);

		// Verificação
		Assert.fail();
	}

	// B.d) Deve verificar existência da conta de destino no banco
	@Test
	public void deveVerificarExistenciaContaBanco() throws
			ContaJaExistenteException, ContaNumeroInvalidoException, ContaPoupancaSaldoNegativoException {
		// Cenario
		int numeroConta = 123;
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, numeroConta, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);
		Conta contaObtida = banco.obterContaPorNumero(numeroConta);

		// Verificação
		assertEquals(conta, contaObtida);
	}

	// B.e) Não deve permitir transferir um valor negativo
	// Solução: Criar uma nova exceção "BancoTransferenciaSaldoInvalidoException" e adicionar às verificações de
	// transferência
	@Test(expected = BancoTransferenciaSaldoInvalidoException.class)
	public void naoDeveTransferirValorNegativo()
			throws ContaPoupancaSaldoNegativoException, ContaNumeroInvalidoException, ContaJaExistenteException,
			ContaSemSaldoException, ContaNaoExistenteException, BancoTransferenciaSaldoInvalidoException {

		// Cenario
		Cliente cliente1 = new Cliente("Joao");
		Conta conta1 = new Conta(cliente1, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 321, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		banco.efetuarTransferencia(conta1.getNumeroConta(), conta2.getNumeroConta(), -5);

		// Verificação
		Assert.fail();
	}

	@Test
	public void deveEfetuarTransferenciaContasCorrentes()
			throws ContaSemSaldoException, ContaNaoExistenteException, ContaPoupancaSaldoNegativoException,
			BancoTransferenciaSaldoInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
}
