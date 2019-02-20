package edu.elte.thesis.messaging.json;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Viktoria Sinkovics
 */
public class JsonObjectMarshaller {

    private static final String ECLIPSELINK_MEDIA_TYPE = "eclipselink.media-type";

    private static final String ECLIPSELINK_JSON_INCLUDE_ROOT = "eclipselink.json.include-root";

    private static final String JSON_FORMAT = "application/json";

    private final Class clazz;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    public JsonObjectMarshaller(Class clazz) {
        Assert.notNull(clazz, "clazz should not be null.");

        this.clazz = clazz;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            this.marshaller = jaxbContext.createMarshaller();
            this.unmarshaller = jaxbContext.createUnmarshaller();

            this.marshaller.setProperty(ECLIPSELINK_MEDIA_TYPE, JSON_FORMAT);
            this.marshaller.setProperty(ECLIPSELINK_JSON_INCLUDE_ROOT, false);
            this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

            this.unmarshaller.setProperty(ECLIPSELINK_MEDIA_TYPE, JSON_FORMAT);
            this.unmarshaller.setProperty(ECLIPSELINK_JSON_INCLUDE_ROOT, false);

        } catch (javax.xml.bind.JAXBException exception) {
            exception.printStackTrace();
        }
        Assert.notNull(marshaller, "marshaller should not be null.");
    }

    public String marshal(Object object) {
        Assert.notNull(object, "object should not be null.");
        Assert.isTrue(this.clazz.equals(object.getClass()) || object.getClass().isInstance(this.clazz),
                "The provided object should be of type " + this.clazz + ", but was " + object.getClass());

        Assert.notNull(marshaller, "marshaller should not be null.");
        StringWriter stringWriter = new StringWriter();
        try {
            this.marshaller.marshal(object, stringWriter);
        } catch (javax.xml.bind.JAXBException exception) {
            exception.printStackTrace();
        }

        return stringWriter.toString();
    }

    public Object unmarshal(String string) {
        Assert.isTrue(!StringUtils.isEmpty(string), "String should not be empty.");

        StringReader stringReader = new StringReader(string);
        Object object = null;
        try {
            object = this.unmarshaller.unmarshal(stringReader);
        } catch (JAXBException exception) {
            exception.printStackTrace();
        }

        return this.clazz.cast(object);
    }
}
