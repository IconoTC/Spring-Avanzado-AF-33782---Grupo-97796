package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.model.ContadorBean;
import com.example.service.FieldInjectionService;
import com.example.service.PersonaService;
import com.example.service.SetterService;

@SpringBootApplication
public class SpringIocLabApplication implements CommandLineRunner {

    private final PersonaService personaService;
    private final SetterService setterService;
    private final FieldInjectionService fieldService;
    private final ApplicationContext ctx;

    public SpringIocLabApplication(PersonaService personaService,
                                   SetterService setterService,
                                   FieldInjectionService fieldService,
                                   ApplicationContext ctx) {
        this.personaService = personaService;
        this.setterService = setterService;
        this.fieldService = fieldService;
        this.ctx = ctx;
    }

    @Override
    public void run(String... args) {
        System.out.println("--- Laboratorio Spring IoC: Inversión de Control ---");

        System.out.println("\n1) Inyección por constructor (PersonaService):");
        personaService.decirHola();

        System.out.println("\n2) Inyección por setter (SetterService):");
        setterService.saludar();

        System.out.println("\n3) Inyección por campo (FieldInjectionService):");
        fieldService.saludar();
        
        var c1 = ctx.getBean(ContadorBean.class);
        var c2 = ctx.getBean(ContadorBean.class);
              System.out.println("\n4) Bean con scope %s:".formatted(c1 == c2 ? "singleton" : "prototype"));
        System.out.println("c1 = %d < %s".formatted(c1.getNext(), c1));
        System.out.println("c2 = %d < %s".formatted(c2.getNext(), c2));
        System.out.println("c1 = %d < %s".formatted(c1.getNext(), c1));
        System.out.println("c2 = %d < %s".formatted(c2.getNext(), c2));
        System.out.println("c1 = %d < %s".formatted(c1.getNext(), c1));
        
        System.out.println("\n5) @Lazy Bean (no inicializado hasta uso):");
        System.out.println("Pidiendo lazyBean...");
        Object lazy = ctx.getBean("saludoLento");
        System.out.println("lazyBean obtenido: " + lazy.getClass().getSimpleName());

        System.out.println("\n6) Uso de @Primary y @Qualifier:");
        personaService.decirHola();
        personaService.mostrarMensajeCalificado();

    }

    public static void main(String[] args) {
        SpringApplication.run(SpringIocLabApplication.class, args);
    }
}
