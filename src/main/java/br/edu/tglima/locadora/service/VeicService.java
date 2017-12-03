package br.edu.tglima.locadora.service;

import static br.edu.tglima.locadora.util.FacesUtil.enviarMsgErro;
import static br.edu.tglima.locadora.util.FacesUtil.enviarMsgSucesso;
import static br.edu.tglima.locadora.util.Util.fmtToSave;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.edu.tglima.locadora.models.veiculo.OpCategorias;
import br.edu.tglima.locadora.models.veiculo.OpMarcas;
import br.edu.tglima.locadora.models.veiculo.OpStatus;
import br.edu.tglima.locadora.models.veiculo.Veiculo;
import br.edu.tglima.locadora.repository.VeiculoRepository;

public class VeicService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private VeiculoRepository repository;

	public Veiculo cadastrar(Veiculo veic) {
		try {
			repository.salvarNovo(fmtToSave(veic));
			enviarMsgSucesso("Veículo cadastrado com sucesso!");
			veic = null;

		} catch (Exception e) {
			enviarMsgErro("Erro, veículo não cadastrado!");

			if (e.getMessage().contains("ConstraintViolationException")) {

				enviarMsgErro("A placa informada já pertence a outro veículo.");

			} else {
				enviarMsgErro(e.getMessage());
			}

		}

		return veic;
	}

	public Veiculo salvarEdicao(Veiculo veic) {
		try {
			repository.salvarEdicao(fmtToSave(veic));
			enviarMsgSucesso("As alterações do veículo " + veic.getPlaca().toUpperCase()
					+ " foram salvas com sucesso!");

			// TODO refazerPesquisa();
		} catch (Exception e) {
			enviarMsgErro("Erro, as alterações não foram salvas!");
			if (e.getMessage().contains("ConstraintViolationException")) {
				enviarMsgErro("A placa informada já pertence a outro veículo.");
			} else {
				enviarMsgErro(e.getMessage());
			}
		}

		return veic;
	}

	public void alterarStatus(Long id, OpStatus status) {
		try {
			Veiculo veic = repository.buscarPorId(id);
			veic.setStatus(status);
			repository.salvarEdicao(fmtToSave(veic));
			enviarMsgSucesso(
					"O Status do veículo " + veic.getPlaca().toUpperCase() + ", foi alterado com sucesso");
		} catch (Exception e) {
			enviarMsgErro("Erro, não foi possível salvar o status do veículo");
			enviarMsgErro(e.getMessage());
		}

	}

	public List<Veiculo> buscarPorStatus(OpStatus status) {
		List<Veiculo> result = new ArrayList<Veiculo>();

		try {
			result = repository.buscaPorStatus(status);
		} catch (Exception e) {
			enviarMsgErro("Erro ao realizar a pesquista! " + e.getMessage());
			result = null;
		}

		return result;
	}

	public List<Veiculo> buscarPorMarcaENaoAlugado(OpMarcas marca) {
		List<Veiculo> result = new ArrayList<Veiculo>();

		try {
			result = repository.buscarPorMarca(marca, OpStatus.ALUGADO, false);
		} catch (Exception e) {
			enviarMsgErro("Erro ao realizar a pesquista! " + e.getMessage());
			result = null;
		}

		return result;
	}

	public List<Veiculo> buscarPorCategoriaENaoAlugado(OpCategorias categoria) {
		List<Veiculo> result = new ArrayList<Veiculo>();

		try {
			result = repository.buscaPorCategoria(categoria, OpStatus.ALUGADO, false);
		} catch (Exception e) {
			enviarMsgErro("Erro ao realizar a pesquista! " + e.getMessage());
			result = null;
		}

		return result;

	}

}
