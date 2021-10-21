package br.com.aula;

import br.com.aula.exception.ContaPoupancaSaldoNegativoException;

public class Conta {

	private Cliente cliente;
	private int numeroConta;
	private int saldo;
	private TipoConta tipoConta;

	public Conta(Cliente cliente, int numeroConta, int saldo, TipoConta tipoConta)
			throws ContaPoupancaSaldoNegativoException {

		if (tipoConta == TipoConta.POUPANCA && saldo < 0) {
			throw new ContaPoupancaSaldoNegativoException();
		}

		this.numeroConta = numeroConta;
		this.cliente = cliente;
		this.saldo = saldo;
		this.tipoConta = tipoConta;
	}

	public void creditar(int valor) {
		this.saldo = this.getSaldo() + valor;
	}

	public void debitar(int valor) throws ContaPoupancaSaldoNegativoException {
		int novoSaldo = this.getSaldo() - valor;

		if (this.tipoConta == TipoConta.POUPANCA && novoSaldo < 0) throw new ContaPoupancaSaldoNegativoException();

		this.saldo = novoSaldo;
	}

	public TipoConta getTipoConta() {
		return tipoConta;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public int getNumeroConta() {
		return numeroConta;
	}

	public int getSaldo() {
		return saldo;
	}
}
