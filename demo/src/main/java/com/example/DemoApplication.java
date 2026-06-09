package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.example.aop.AuthenticationService;
import com.example.aop.introductions.Visible;
import com.example.base.DummyJSpecify;
import com.example.ioc.ConstructorConValores;
import com.example.ioc.GenericoEvent;
import com.example.ioc.NotificationService;
import com.example.ioc.Rango;
import com.example.ioc.anotaciones.Twit;
import com.example.ioc.contratos.Configuracion;
import com.example.ioc.contratos.ServicioCadenas;
import com.example.ioc.notificaciones.Sender;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy
public class DemoApplication implements CommandLineRunner {
//	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DemoApplication.class);
	
	private final ConstructorConValores miClase;

	DemoApplication(ConstructorConValores miClase) {
		this.miClase = miClase;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicación arrancada...");
		log.warn("Aplicación arrancada...");
	}

	@PreDestroy
	void Cierre() {
		System.err.println("Aplicación cerrada...");
	}
	
//	@Bean
	CommandLineRunner nulos(/*DummyJSpecify dummy*/) {
		return arg -> {
			try {
				var dummy = new DummyJSpecify("Hola mundo");
				System.err.println(dummy.getClass().getCanonicalName());
				dummy.setCadenaSegura(null);
//				System.out.println(dummy.getCadena().toUpperCase());
				if(dummy.getCadenaSegura().isPresent())
				System.out.println(dummy.getCadenaSegura().get().toUpperCase());
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
	
	@Bean
	CommandLineRunner ioc(NotificationService notify, ServicioCadenas srv, ConstructorConValores miOtraClase, 
			@Value("${mi.valor:Sin valor}") String miValor, Rango rango, AuthenticationService auth
) {
		return arg -> {
			try {
//				NotificationService notify = new NotificationServiceImpl();
//				ServicioCadenas srv = new ServicioCadenasImpl(new RepositorioCadenasImpl(new ConfiguracionImpl(notify), notify), notify);
				
				miOtraClase.titulo(miOtraClase.getAutor());
				notify.add("Hola mundo");
				System.err.println(srv.getClass().getCanonicalName());
				auth.login();
				srv.add("Uno nuevo");
				notify.add(miValor);
				notify.add(rango.toString());
				notify.add(Integer.toString(rango.getMax()));
				System.out.println("=================================>");
				notify.getListado().forEach(System.out::println);
				System.out.println("<=================================");
				if(srv instanceof Visible v) {
					System.out.println(v.isVisible() ? "Ahora me ves" : "Ahora NO me ves");
					v.mostrar();
					System.out.println(v.isVisible() ? "Ahora me ves" : "Ahora NO me ves");
					v.ocultar();
					System.out.println(v.isVisible() ? "Ahora me ves" : "Ahora NO me ves");
				} else 
					System.out.println("No implementa el interfaz Visible");
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
//	@Bean
	CommandLineRunner porNombre(@Twit Sender sender, List<Sender> todos) {
		return arg -> {
			try {
				sender.send("envía notidicación");
				todos.forEach(s -> s.send("mensaje"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
	
//	@Bean
	CommandLineRunner configuracionEnXML() {
		return _ -> {
			try (var contexto = new FileSystemXmlApplicationContext("applicationContext.xml")) {
				var notify = contexto.getBean(NotificationService.class);
				System.out.println("configuracionEnXML ===================>");
				var srv = (ServicioCadenas) contexto.getBean("servicioCadenas");
				System.out.println(srv.getClass().getName());
				contexto.getBean(NotificationService.class).getListado().forEach(System.out::println);
				System.out.println("===================>");
				srv.get().forEach(notify::add);
				srv.add("Hola mundo");
				notify.add(srv.get(1));
				srv.modify("modificado");
				System.out.println("===================>");
				notify.getListado().forEach(System.out::println);
				notify.clear();
				System.out.println("<===================");
				((Sender) contexto.getBean("sender")).send("Hola mundo");
			}
		};
	}

//	@EventListener
//	void eventHandlerGenerico(GenericoEvent event) {
//		System.err.println("Evento recibido de %s: %s".formatted(event.origen(), event.carga()));
//	}
//
//	@EventListener
//	void eventHandlerGenericoOtro(GenericoEvent event) {
//		System.err.println("Otro tratamiento de %s: %s".formatted(event.origen(), event.carga()));
//	}
//
//	@EventListener
//	void eventHandlerCadena(String event) {
//		System.err.println("Evento cadena: %s".formatted(event));
//	}
}
