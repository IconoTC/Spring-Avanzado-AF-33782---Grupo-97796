package com.example;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.aop.AspectoImplAspect;
import com.example.aop.AuthenticationService;
import com.example.aop.introductions.Visible;
import com.example.base.DummyAsync;
import com.example.base.DummyJSpecify;
import com.example.base.DummyRetry;
import com.example.ioc.ConstructorConValores;
import com.example.ioc.NotificationService;
import com.example.ioc.Rango;
import com.example.ioc.anotaciones.Twit;
import com.example.ioc.contratos.ServicioCadenas;
import com.example.ioc.notificaciones.Sender;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
@EnableAsync
public class DemoApplication implements CommandLineRunner {
//	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DemoApplication.class);

//	private final ConstructorConValores miClase;
//
//	DemoApplication(ConstructorConValores miClase) {
//		this.miClase = miClase;
//	}

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
	CommandLineRunner nulos(/* DummyJSpecify dummy */) {
		return arg -> {
			try {
				var dummy = new DummyJSpecify("Hola mundo");
				System.err.println(dummy.getClass().getCanonicalName());
				dummy.setCadenaSegura(null);
//				System.out.println(dummy.getCadena().toUpperCase());
				if (dummy.getCadenaSegura().isPresent())
					System.out.println(dummy.getCadenaSegura().get().toUpperCase());
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

//	@Bean
	CommandLineRunner ioc(NotificationService notify, ServicioCadenas srv, ConstructorConValores miOtraClase,
			@Value("${mi.valor:Sin valor}") String miValor, Rango rango, AuthenticationService auth) {
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
//				System.out.println("=================================>");
//				notify.getListado().forEach(System.out::println);
//				System.out.println("<=================================");
				if (srv instanceof Visible v) {
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

//	@Bean
	CommandLineRunner generarProxyAOPManualmente() {
		return arg -> {
			try {
				var dummy = new DummyJSpecify("Hola mundo");
				AspectJProxyFactory factory = new AspectJProxyFactory(dummy);
				factory.addAspect(AspectoImplAspect.class);
				dummy = factory.getProxy();

				System.err.println(dummy.getClass().getCanonicalName());
				dummy.setCadenaSegura(null);
//				System.out.println(dummy.getCadena().toUpperCase());
				if (dummy.getCadenaSegura().isPresent())
					System.out.println(dummy.getCadenaSegura().get().toUpperCase());
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}


	@Autowired
	NotificationService notify;
	
	@Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 5)
	void tareasProgramadas() {
//		System.out.println("Han pasado 5 segundos.");
		if(!notify.hasMessages()) return;
		System.out.println("=================================>");
		notify.getListado().forEach(System.out::println);
		notify.clear();
		System.out.println("<=================================");
	}

//	@Bean
	CommandLineRunner asincrono(DummyAsync dummy) {
		return arg -> {
			var obj = dummy; // new DummyAsync();
			System.err.println(obj.getClass().getCanonicalName());
//			obj.ejecutarAutoInvocado(1);
//			obj.ejecutarAutoInvocado(2);
			obj.ejecutarTareaSimpleAsync(1);
			obj.ejecutarTareaSimpleAsync(2);
			obj.calcularResultadoAsync(10, 20, 30, 40, 50).thenAccept(result -> notify.add(result));
			obj.calcularResultadoAsync(1, 2, 3).thenAccept(result -> notify.add(result));
			obj.calcularResultadoAsync().thenAccept(result -> notify.add(result));
			System.err.println("Termino de mandar hacer las cosas");
		};
	}

//	@Bean
	CommandLineRunner resiliencia(DummyRetry dummy) {
		return arg -> {
			try {
				IO.println("------------------> reintentaConAnotacion: " + dummy.reintentaConAnotacion(3));
				IO.println("------------------> reintentaConAnotacion: " + dummy.reintentaConAnotacion(5));
			} catch (Exception e) {
				System.err.println("ERROR reintentaConAnotacion: " + e.getMessage());
			}
//			dummy.reinicia();
			try {
				IO.println("------------------> reintentaConTemplate: " + dummy.reintentaConTemplate(3));
				IO.println("------------------> reintentaConTemplate: " + dummy.reintentaConTemplate(5));
			} catch (Exception e) {
				System.err.println("ERROR reintentaConTemplate: " + e.getMessage());
			}
		};
	}

}
