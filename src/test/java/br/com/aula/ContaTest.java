package br.com.aula;

import static org.junit.Assert.assertEquals;

import br.com.aula.exception.ContaJaExistenteException;
import br.com.aula.exception.ContaNumeroInvalidoException;
import br.com.aula.exception.ContaPoupancaSaldoNegativoException;
import org.junit.Assert;
import org.junit.Test;



public class ContaTest {
	@Test
	public void deveCreditar() throws ContaPoupancaSaldoNegativoException {
		// Cenario
		Cliente cliente = new Cliente("João");
		Conta c = new Conta(cliente, 123, 10, TipoConta.CORRENTE);

		// Ação
		c.creditar(5);

		// Verificação
		assertEquals(15, c.getSaldo());
	}
}
