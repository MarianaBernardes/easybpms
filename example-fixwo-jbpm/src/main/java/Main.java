import org.fixwo.dao.CRUDOccurrence;
import org.fixwo.domain.Occurrence;

public class Main {

	public static void main(String[] args) {
		//Instancia Ocorrencia
		Occurrence o = new Occurrence();
		o.setId(1L);	
				
		CRUDOccurrence crud = new CRUDOccurrence();
		
		//Iniciar o processo
		crud.create(o);
		
		//Executar a atividade Classificar e encaminhar ao setor responsavel
		o.setSector("Limpeza");
		o.setStatus("Em andamento");
		crud.update(o);
				
		//Executar a atividade Feedback ao solicitante
		o.setFeedback("Papel toalha colocado no banheiro");
		o.setStatus("Finalizado");
		crud.update(o);
		
		//Executar a atividade Avaliar Solucao
		o.setEvaluation(false);
		crud.update(o);

	}

}
