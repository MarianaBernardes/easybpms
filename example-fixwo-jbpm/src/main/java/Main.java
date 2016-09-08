import org.fixwo.dao.OcorrenciaDAO;
import org.fixwo.domain.Ocorrencia;

public class Main {

	public static void main(String[] args) {
		//Instancia Ocorrencia
		Ocorrencia o = new Ocorrencia();
		o.setId(1L);	
				
		OcorrenciaDAO crud = new OcorrenciaDAO();
		
		//Iniciar o processo
		crud.create(o);
		
		//Executar a atividade Classificar e encaminhar ao setor responsavel
		o.setSetor("Limpeza");
		o.setStatus("Em andamento");
		crud.update(o);
				
		//Executar a atividade Feedback ao solicitante
		o.setFeedback("Papel toalha colocado no banheiro");
		o.setStatus("Finalizado");
		crud.update(o);
		
		//Executar a atividade Avaliar Solucao
		o.setAvaliacao(false);
		crud.update(o);

	}

}
