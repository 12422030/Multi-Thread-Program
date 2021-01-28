package tf.epccfe.entity;

import java.io.StringReader;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

public class Xml2Entity {

    private IBindingFactory factory;
    private StringReader reader;
    
	public MsgHeader xml2MsgHeader(String xmlMsgHeader) throws Exception {

        factory = BindingDirectory.getFactory(MsgHeader.class);
        reader = new StringReader(xmlMsgHeader);
        IUnmarshallingContext uctx = factory.createUnmarshallingContext();
        Object result = uctx.unmarshalDocument(reader);
        reader.close();
        reader = null;
        return (MsgHeader) result;
	}
	
	public Epcc999Body xml2Epcc999Body(String xmlMsgBody) throws Exception {

        factory = BindingDirectory.getFactory(Epcc999Body.class);
        reader = new StringReader(xmlMsgBody);
        IUnmarshallingContext uctx = factory.createUnmarshallingContext();
        Object result = uctx.unmarshalDocument(reader);
        reader.close();
        reader = null;
        return (Epcc999Body) result;
	}
}
