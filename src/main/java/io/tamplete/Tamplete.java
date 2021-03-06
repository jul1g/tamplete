package io.tamplete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Engine
 */
public class Tamplete {

    private final Logger logger = LoggerFactory.getLogger(Tamplete.class);

    Map<String, CompiledScript> compiledScrips = new HashMap<String, CompiledScript>();

    Transformer transformer = new Transformer();

    static public void main(String[] args) throws Exception {
        Tamplete plop = new Tamplete();

        System.out.println(plop.run("template/donothing.tmpl", false));
        //System.out.println(plop.run("script.js", true));

    }


    private ScriptEngine getScriptEngine() {

        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");

        if (engine == null) {
            engine = factory.getEngineByName("JavaScript");
        }

        return engine;

    }


    public StringBuilder run(String templateName, boolean compile) throws Exception {

        logger.debug("executing {} in compile:{} mode", templateName, compile);

        StringBuilder writer = new StringBuilder();


        ScriptEngine engine = getScriptEngine();

        InputStreamReader reader = getTemplate(templateName);
        Bindings bindings = getDefaultBinding(engine);
        bindings.put("writer", writer);
        bindings.put("testString", "test value share ");

        if (compile) {

            CompiledScript script = compiledScrips.get(templateName);

            if (engine instanceof Compilable) {

                if (script == null) {
                    Compilable compilingEngine = (Compilable) engine;
                    script = compilingEngine.compile(reader);
                    compiledScrips.put(templateName, script);
                    script.eval(bindings);
                }
            }

        } else {
            engine.eval(reader, bindings);
        }



        return writer;
    }

    private Bindings getDefaultBinding(ScriptEngine engine) {
        return engine.createBindings();
    }


    private InputStreamReader getTemplate(String name) {

        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return inputStreamReader;
    }

}

