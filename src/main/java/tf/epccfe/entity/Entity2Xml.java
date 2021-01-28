package tf.epccfe.entity;

import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import tf.epccfe.sys.Sys;

public class Entity2Xml {

    private IBindingFactory factory;
    private StringWriter writer;
    
	public String entity2XmlRoot(Object entity) throws Exception {

    	factory = BindingDirectory.getFactory(entity.getClass());
    	writer = new StringWriter();
    	IMarshallingContext mctx = factory.createMarshallingContext();
    	mctx.setIndent(2); // 缩进空格数
    	mctx.marshalDocument(entity, Sys.CHARSET_NAME, null, writer);
    	String xmlStr = writer.toString();
		int rootB = xmlStr.indexOf("<root");
		int rootE = xmlStr.lastIndexOf("</root>");
    	return xmlStr.substring(rootB, rootE + 7);
	}
}
