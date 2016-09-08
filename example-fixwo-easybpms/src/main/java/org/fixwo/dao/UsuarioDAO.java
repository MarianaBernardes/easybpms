package org.fixwo.dao;

import java.util.List;

import org.fixwo.domain.Usuario;

import com.easybpms.db.dao.CRUDUser;

public class UsuarioDAO {
	
	public static void create(String nomeUsuario, String id, String tenancy, List<String> grupoNomes) {
		Usuario usuario = new Usuario();
		usuario.setName(nomeUsuario);
		usuario.setIdApp(id);
		usuario.setTenancy(tenancy);
		usuario.setUserGroupNames(grupoNomes);
		
		try {	
			CRUDUser.create(usuario); 
		
		} catch (Exception ex) {			
			ex.printStackTrace();		
		}
	}

}
