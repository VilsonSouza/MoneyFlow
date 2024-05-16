package controller;

import java.util.ArrayList;
import java.util.Date;

import model.dao.*;
import model.vo.*;

public class MoneyFlowController {
	
	private QuadroDAO quadroDAO;
	private MovimentacaoDAO movimentacaoDAO;
	private BancoDAO bancoDAO;
	private MetaDAO metaDAO;
	private CategoriaDAO categoriaDAO;
	private UsuarioDAO usuarioDAO;

	
	public MoneyFlowController() {
		quadroDAO = new QuadroDAO();
		bancoDAO = new BancoDAO();
		metaDAO = new MetaDAO();
		categoriaDAO = new CategoriaDAO();
		usuarioDAO = new UsuarioDAO();
		movimentacaoDAO = new MovimentacaoDAO();
	}
	
	public ArrayList<QuadroVO> getQuadros(String emailUsuario, String pesquisa, String filtro) {
		return quadroDAO.getQuadros(emailUsuario, pesquisa, filtro);
	}

	public int addQuadro(QuadroVO quadroVO) {
		return quadroDAO.addQuadro(quadroVO);
	}

	public boolean alterQuadro(QuadroVO quadroVO) {
		return quadroDAO.alterQuadro(quadroVO);
	}

	public boolean delQuadro(int codigo) {
		return quadroDAO.delQuadro(codigo);
	}

	public ArrayList<MovimentacaoVO> getMovimentacoes(int codigo, String emailUsuario, String pesquisa, String filtro, Date de, Date ate) {
		return movimentacaoDAO.getMovimentacoes(codigo, emailUsuario, pesquisa, filtro, de, ate);
	}

	public ArrayList<BancoVO> getBancos(String emailUsuario, String pesquisa, String filtro) {
		return bancoDAO.getBancos(emailUsuario, pesquisa, filtro);
	}

	public int addBanco(String emailUsuario, BancoVO bancoVO) {
		return bancoDAO.addBanco(emailUsuario, bancoVO);
	}

	public boolean alterBanco(BancoVO bancoVO) {
		return bancoDAO.alterBanco(bancoVO);
	}

	public boolean delBanco(int codigo) {
		return bancoDAO.delBanco(codigo);
	}

	public ArrayList<MetaVO> getMetas(String emailUsuario, String pesquisa, String filtro) {
		return metaDAO.getMetas(emailUsuario, pesquisa, filtro);
	}

	public int addMeta(String emailUsuario, MetaVO metaVO) {
		return metaDAO.addMeta(emailUsuario, metaVO);
	}

	public boolean alterMeta(String emailUsuario, MetaVO metaVO) {
		return metaDAO.alterMeta(emailUsuario, metaVO);
	}

	public boolean delMeta(String emailUsuario, int codigo) {
		return metaDAO.delMeta(emailUsuario, codigo);
	}

	public boolean verificaUsuario(UsuarioVO usuarioVO) {
		return usuarioDAO.verificaUsuario(usuarioVO);
	}

	public boolean addUsuario(UsuarioVO usuarioVO) {
		return usuarioDAO.addUsuario(usuarioVO);
	}

	public boolean verificaUsuario(String email) {
		return usuarioDAO.verificaUsuario(email);
	}

	public int getQtdBancos(String emailUsuario) {
		return bancoDAO.getQtdBancos(emailUsuario);
	}
	
	public boolean delUsuario(String email) {
		return usuarioDAO.delUsuario(email);
	}
	
	public boolean alterUsuario(UsuarioVO usuarioVO) {
		return usuarioDAO.alterUsuario(usuarioVO);
	}
	
	public UsuarioVO getUsuario(String email) {
		return usuarioDAO.getUsuario(email);
	}
	
	public ArrayList<CategoriaVO> getCategorias(String emailUsuario, String pesquisa, String filtro) {
		return categoriaDAO.getCategoria(emailUsuario, pesquisa, filtro);
	}

	public int getQtCategorias(String emailUsuario) {
		return categoriaDAO.getQtdCategorias(emailUsuario);
	}

	public boolean addMovimentacao(String emailUsuario, MovimentacaoVO movimentacaoVO) {
		return movimentacaoDAO.addMovimentacao(emailUsuario, movimentacaoVO);
	}

	public MovimentacaoVO getMovimentacao(int codigo) {
		return movimentacaoDAO.getMovimentacao(codigo);
	}

	public boolean alterMovimentacao(String emailUsuario, MovimentacaoVO movimentacaoVO) {
		return movimentacaoDAO.alterMovimentacao(emailUsuario, movimentacaoVO);
	}

	public boolean ehNumero(String text) {
		return movimentacaoDAO.ehNumero(text);
	}

	public boolean delMovimentacao(int codigo) {
		return movimentacaoDAO.delMovimentacao(codigo);
	}

	public boolean delCategoria(int codigo) {
		return categoriaDAO.delCategoria(codigo);
	}

	public int addCategoria(String emailUsuario, CategoriaVO categoriaVO) {
		return categoriaDAO.addCategoria(emailUsuario, categoriaVO);
	}

	public boolean alterCategoria(CategoriaVO categoriaVO) {
		return categoriaDAO.alterCategoria(categoriaVO);
	}
	
	public boolean atualizaValoresBanco(String emailUsuario) {
		return bancoDAO.atualizaValores(emailUsuario);
	}
	
	public boolean atualizaValoresMeta(String emailUsuario) {
		return metaDAO.atualizaValores(emailUsuario);
	}
	
	public boolean atualizaValoresQuadro(String emailUsuario) {
		return quadroDAO.atualizaValores(emailUsuario);
	}

	public boolean delMovimentacaoQuadro(String emailUsuario, int codigoQuadro) {
		return movimentacaoDAO.delMovimentacaoQuadro(emailUsuario, codigoQuadro);
	}

	public boolean delMovimentacaoBanco(String emailUsuario, int codigoBanco) {
		return movimentacaoDAO.delMovimentacaoBanco(emailUsuario, codigoBanco);
	}

	public boolean delMovimentacaoCategoria(String emailUsuario, int codigoCategoria) {
		return movimentacaoDAO.delMovimentacaoCategoria(emailUsuario, codigoCategoria);
	}

	public boolean alterMovimentacaoMeta(String emailUsuario, int codigoMeta) {
		return movimentacaoDAO.alterMovimentacaoMeta(emailUsuario, codigoMeta);
	}

	public ArrayList<RelatorioAnualVO> getRelatorioAnual(String emailUsuario) {
		return usuarioDAO.getRelatorioAnual(emailUsuario);
	}

	public boolean enviarEmail(String emailReceiptient, String emailSubject, String emailBody) {
		return usuarioDAO.enviarEmail(emailReceiptient, emailSubject, emailBody);
	}

	public String enviarConfirmacaoCadastro(String email, String nome) {
		return usuarioDAO.enviarConfirmacaoCadastro(email, nome);
	}

	public String enviarConfirmacaoDelUsuario(String email, String nome) {
		return usuarioDAO.enviarConfirmacaoDelUsuario(email, nome);
	}
}
