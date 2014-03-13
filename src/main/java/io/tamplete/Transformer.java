package io.tamplete;

/**
 *
 */
public class Transformer {


    public String transform(String template) {

        String scriptJS = "writer.append(\"" + template + "\")";

System.out.print(scriptJS);

        return scriptJS;
    }

}
