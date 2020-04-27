package accesoDatos;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.*;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class HibernateManager implements I_Acceso_Datos{
	private SessionFactory sf;
	private Session s;
	
	public HibernateManager() {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		 sf = new Configuration().configure().buildSessionFactory();
		

	}

	private void iniciaTransaccion() {
		s = sf.openSession();
		s.beginTransaction();
	}

	private void terminaTransaccion() {
		s.getTransaction().commit();
		s.close();
	}


	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {
		HashMap<Integer,Deposito> map = new HashMap<Integer, Deposito>();
		iniciaTransaccion();
		Query query = s.createQuery("SELECT e FROM Deposito e");
		List<Deposito> list=query.list();
		for (Deposito deposito : list) {
			int valor=deposito.getValor();
			map.put(valor,deposito);
		}
		terminaTransaccion();
		return map;
	
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String,Dispensador> mapdispen = new HashMap<String, Dispensador>();
		iniciaTransaccion();
		Query query = s.createQuery("SELECT e FROM Dispensador e");
		List<Dispensador> list=query.list();
		for (Dispensador dispensador : list) {
			String clave=dispensador.getClave();
			mapdispen.put(clave,dispensador);
		}
		terminaTransaccion();
		return mapdispen;
	
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean todoOK = true;
		iniciaTransaccion();
		for (Entry<Integer, Deposito> entry : depositos.entrySet()) {
			Deposito cantidadDeposito = entry.getValue();
			
			int cantidad = cantidadDeposito.getCantidad();
			int valor = cantidadDeposito.getValor();
			Query query = s.createQuery("UPDATE Deposito e SET e.cantidad = :cantidad WHERE e.valor = :valor");
			query.setParameter("cantidad", cantidad);
			query.setParameter("valor", valor);
			query.executeUpdate();
		}
		terminaTransaccion();
		return todoOK;
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean todoOK = true;
		iniciaTransaccion();
		for (Entry<String, Dispensador> entry : dispensadores.entrySet()) {
			Dispensador cantidadDispensador = entry.getValue();

			int cantidad = cantidadDispensador.getCantidad();
			String clave = cantidadDispensador.getClave();
			Query query = s.createQuery("UPDATE Dispensador e SET e.cantidad = :cantidad WHERE e.clave = :clave");
			query.setParameter("cantidad", cantidad);
			query.setParameter("clave", clave);
			query.executeUpdate();
		}
		terminaTransaccion();
		return todoOK;
	}

}
