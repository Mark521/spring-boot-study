package mark.component.dbdialect.def;

import mark.component.dbmodel.model.Schema;
import mark.component.dbmodel.model.SystemFunction;
import mark.component.dbmodel.model.SystemFunction.Arg;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/** 
 * 
 * @author liang
 * DOM 解析XML文档 
 */
class ParserFunction {

    public static Map<String, SystemFunction> parserFunctions(InputStream is) throws Exception {
        Map<String, SystemFunction> systemFunctions = new HashMap<String, SystemFunction>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(is);
        NodeList functions = document.getChildNodes();

        final Schema schema = new Schema();
        for (int i = 0; i < functions.getLength(); i++) {
            Node function = functions.item(i);
            NodeList functionDef = function.getChildNodes();
            for (int j = 0; j < functionDef.getLength(); j++) {
                Node node = functionDef.item(j);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList functionMeta = node.getChildNodes();
                    String name = null;
                    List<Arg> args = new ArrayList<Arg>();
                    String syntax = null;
                    String description = null;
                    List<String> examples = new ArrayList<String>();
                    for (int k = 0; k < functionMeta.getLength(); k++) {
                        Node c = functionMeta.item(k);
                        if (c.getNodeType() == Node.ELEMENT_NODE) {
                            Node fn = functionMeta.item(k);
                            String cName = fn.getNodeName();
                            if (cName.equals("syntax")) {
                                syntax = fn.getTextContent();
                            } else if (cName.equals("name")) {
                                name = fn.getTextContent();
                            } else if (cName.equals("description")) {
                                description = fn.getTextContent();
                            } else if (cName.equals("args")) {
                                NodeList nArgs = c.getChildNodes();
                                for (int m = 0; m < nArgs.getLength(); m++) {
                                    Node nArg = nArgs.item(m);
                                    if (nArg.getNodeType() == Node.ELEMENT_NODE) {
                                        NamedNodeMap attributes = nArg.getAttributes();
                                        Node optional = attributes.getNamedItem("optional");
                                        Node va = attributes.getNamedItem("va");
                                        Arg arg = null;
                                        if (optional != null) {
                                            if (optional.getNodeValue().equals("false")) {
                                                String argText = nArg.getTextContent();
                                                arg = new SystemFunction.Arg(Arrays.asList(argText.split(",")));
                                                //System.out.println(nArg.getTextContent());
                                            } else {
                                                String argText = nArg.getTextContent();
                                                arg = new SystemFunction.OptionalArg(Arrays.asList(argText.split(",")));
                                                //System.out.println("optional : " + nArg.getTextContent());
                                            }
                                        } else if (va != null) {
                                            String argText = nArg.getTextContent();
                                            arg = new SystemFunction.VaArg(Arrays.asList(argText.split(",")));
                                            //System.out.println("va : " + nArg.getTextContent());
                                        } else {
                                            String argText = nArg.getTextContent();
                                            arg = new SystemFunction.Arg(Arrays.asList(argText.split(",")));
                                            //System.out.println(nArg.getTextContent());
                                        }
                                        args.add(arg);
                                    }
                                }
                            } else if (cName.equals("examples")) {
                                NodeList nExamples = c.getChildNodes();
                                for (int m = 0; m < nExamples.getLength(); m++) {
                                    Node nExample = nExamples.item(m);
                                    if (nExample.getNodeType() == Node.ELEMENT_NODE) {
                                        String example = nExample.getTextContent().trim();
                                        examples.add(example);
                                        //System.out.println(nExample.getTextContent().trim());
                                    }
                                }
                            } else {
                                throw new RuntimeException("ParserFunction.parserFunctions");
                            }
                        }
                    }
                    SystemFunction systemFunction = new SystemFunction(schema, name, args, syntax, description, examples);
                    systemFunctions.put(name, systemFunction); 
                    //System.out.println("\n\n");
                }
            }
        }
        return systemFunctions;
    }

    public static Map<String, SystemFunction> parserFunctions(String fileName) throws Exception {

        InputStream is = new FileInputStream(fileName);
        return parserFunctions(is);
    }

    public static void main(String[] margs) {
        try {
            final String systemFunctionFilename = "/functions/oracle_12c_system_functions.xml";
            InputStream fis = ParserFunction.class.getResourceAsStream(systemFunctionFilename);
            Map<String, SystemFunction> systemFunctions = parserFunctions(fis);

            for (SystemFunction systemFunction : systemFunctions.values()) {
                System.out.println("name = " + systemFunction.getName());
                System.out.println("syntax = " + systemFunction.getSyntax());
                List<Arg> args = systemFunction.getArgs();
                for (Arg arg : args) {
                    System.out.println(Arrays.asList(arg.getJdbcTypes()));
                }
                List<String> examples = systemFunction.getExamples();
                for (String example : examples) {
                    System.out.println(example);
                }
                System.out.println("description = " + systemFunction.getDescription());

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
