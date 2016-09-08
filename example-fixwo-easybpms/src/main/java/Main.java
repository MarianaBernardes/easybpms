import java.util.ArrayList;
import java.util.List;

import org.fixwo.dao.OcorrenciaDAO;
import org.fixwo.dao.UsuarioDAO;
import org.fixwo.domain.Ocorrencia;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.easybpms.bpms.BpmsSession;
import com.easybpms.codegen.AbstractContext;

public class Main {

	public static void main(String[] args) {
		
		//Carregar ambiente
		//ApplicationContext context = new ClassPathXmlApplicationContext("spring/resources.xml");
		AbstractContext.getContext().connect();
		
		//Iniciar sessão jbpm
		//iniciarSessaoBpms(context);
		
		
		
		//Usuarios
		List<String> grupoNomes = new ArrayList<String>();
		grupoNomes.add("Responsavel Setor");
		UsuarioDAO.create("Responsavel Setor1","1","1",grupoNomes);
		
		grupoNomes = new ArrayList<String>();
		grupoNomes.add("Triador");
		UsuarioDAO.create("Triador1","1","1",grupoNomes);
		
		grupoNomes = new ArrayList<String>();
		grupoNomes.add("Solicitante");
		UsuarioDAO.create("Solicitante1","1","1",grupoNomes);
		
		
		
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
		o.setAvaliacao(false);;
		crud.update(o);
		
	}
	
	/*public static void iniciarSessaoBpms(ApplicationContext context){
		BpmsSession bpmsSession = (BpmsSession) context.getBean("bpmsSession");
		AbstractContext.getContext().setBpmsSession(bpmsSession);
	}*/
}
