package io.determann.serialization.xml;

import io.determann.serialization.view.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class Xml
{
   static final String NAMESPACE = "io.determann.xml";

   public static PropertySetting attribute()
   {
      return new SettingImpl(NAMESPACE, "ATTRIBUTE", true);
   }

   public static TypeSetting root(String root)
   {
      return new SettingImpl(NAMESPACE, "ROOT", root);
   }

   public static <TYPE> TYPE deSerializeXml(DeSerializationView<TYPE, ?> structure, String s)
   {
      try
      {
         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document document = builder.parse(new InputSource(new StringReader(s)));
         document.getDocumentElement().normalize();

         return deSerialize(structure, document::getElementsByTagName);
      }
      catch (ParserConfigurationException | IOException | SAXException e)
      {
         throw new RuntimeException(e);
      }
   }

   private static <TYPE> TYPE deSerialize(DeSerializationView<TYPE, ?> structure, Function<String, NodeList> getNodeList)
   {
      switch (structure)
      {
         case ConstructorDeSerializationView<?> structure1 ->
         {
            //noinspection unchecked
            return deSerialize(((ConstructorDeSerializationView<TYPE>) structure1), getNodeList);
         }
         case PropertyDeSerializationView<?> structure1 ->
         {
            //noinspection unchecked
            PropertyDeSerializationView<TYPE> structure2 = (PropertyDeSerializationView<TYPE>) structure1;
            return deSerialize(structure2.getInstanceSupplier(), structure2.getProperties(), getNodeList);
         }
         case PropertyView<?> structure1 ->
         {
            //noinspection unchecked
            PropertyView<TYPE> structure2 = (PropertyView<TYPE>) structure1;
            List<? extends DeSerializationProperty<TYPE, Object, ?>> properties = structure2.getProperties()
                                                                                            .stream()
                                                                                            .map(property -> ((DeSerializationProperty<TYPE, Object, ?>) property))
                                                                                            .toList();

            return deSerialize(structure2.getInstanceSupplier(), properties, getNodeList);
         }
      }
   }

   private static <TYPE> TYPE deSerialize(ConstructorDeSerializationView<TYPE> structure, Function<String, NodeList> getNodeList)
   {
      switch (structure.getConstructor())
      {
         case Constructor0<TYPE> v ->
         {
            return v.create();
         }
         case Constructor1<TYPE, ?> v ->
         {
            //noinspection unchecked
            Constructor1<TYPE, Object> constructor = (Constructor1<TYPE, Object>) v;
            //noinspection unchecked
            Parameter<TYPE, Object> parameter = (Parameter<TYPE, Object>) structure.get(0);
            Node node = getNodeList.apply(parameter.getName()).item(0);

            return constructor.create(deSerialize(parameter.getType(), parameter::getNestedWrite, node));
         }
         case Constructor2<TYPE, ?, ?> v ->
         {
            //noinspection unchecked
            Constructor2<TYPE, Object, Object> constructor = (Constructor2<TYPE, Object, Object>) v;
            //noinspection unchecked
            Parameter<TYPE, Object> parameter0 = (Parameter<TYPE, Object>) structure.get(0);
            Node node0 = getNodeList.apply(parameter0.getName()).item(0);
            //noinspection unchecked
            Parameter<TYPE, Object> parameter1 = (Parameter<TYPE, Object>) structure.get(1);
            Node node1 = getNodeList.apply(parameter1.getName()).item(0);
            return constructor.create(deSerialize(parameter0.getType(), parameter0::getNestedWrite, node0),
                                      deSerialize(parameter1.getType(), parameter1::getNestedWrite, node1));
         }
      }
   }

   private static <TYPE> TYPE deSerialize(Supplier<TYPE> instanceSupplier,
                                          List<? extends DeSerializationProperty<TYPE, Object, ?>> properties,
                                          Function<String, NodeList> getNodeList)
   {
      TYPE instance = instanceSupplier.get();

      for (DeSerializationProperty<TYPE, Object, ?> property : properties)
      {
         NodeList nodeList = getNodeList.apply(property.getName());
         Node item = nodeList.item(0);

         property.getSetter().accept(instance, deSerialize(property.getType(), property::getNestedWrite, item));
      }

      return instance;
   }

   private static Object deSerialize(Class<Object> type, Supplier<Optional<DeSerializationView<Object, ?>>> nestedSupplier, Node item)
   {
      if (item.getNodeType() == Node.ELEMENT_NODE)
      {
         Element element = (Element) item;
         Optional<DeSerializationView<Object, ?>> nested = nestedSupplier.get();

         if (nested.isPresent())
         {
            return deSerialize(nested.get(), element::getElementsByTagName);
         }
         return parse(type, element.getTextContent());
      }
      if (item.getNodeType() == Node.ATTRIBUTE_NODE)
      {
         Attr attr = (Attr) item;
         return parse(type, attr.getValue());
      }
      throw new IllegalStateException();
   }

   private static final Map<Class<?>, Function<String, Object>> PARSERS = initParsers();

   private static Map<Class<?>, Function<String, Object>> initParsers()
   {
      Map<Class<?>, Function<String, Object>> result = new HashMap<>();

      addParser(result, String.class, s -> s);
      addParser(result, Long.class, Long::valueOf);
      addParser(result, Integer.class, Integer::valueOf);

      return result;
   }

   private static <T> void addParser(Map<Class<?>, Function<String, Object>> parsers, Class<T> type, Function<String, T> parser)
   {
      //noinspection unchecked
      parsers.put(type, (Function<String, Object>) parser);
   }

   private static Object parse(Class<Object> type, String s)
   {
      Function<String, Object> parser = PARSERS.get(type);
      if (parser == null)
      {
         throw new RuntimeException("No parser found for " + type);
      }
      return parser.apply(s);
   }

   public static <TYPE> String serializeXml(SerializationView<TYPE, ?> structure, TYPE instance)
   {
      String rootElementName = (String) structure.getSetting(NAMESPACE, "ROOT").orElseThrow().getValue();
      try
      {
         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document document = builder.newDocument();
         Element root = document.createElement(rootElementName);
         document.appendChild(root);

         serializeInstance(instance, structure, root, document);

         DOMSource domSource = new DOMSource(root);
         Transformer transformer = TransformerFactory.newInstance().newTransformer();
         StringWriter stringWriter = new StringWriter();
         transformer.transform(domSource, new StreamResult(stringWriter));

         return stringWriter.toString();
      }
      catch (ParserConfigurationException | TransformerException e)
      {
         throw new RuntimeException(e);
      }
   }

   private static <TYPE> void serializeInstance(TYPE instance, SerializationView<TYPE, ?> structure, Element parent, Document document)
   {
      //noinspection unchecked
      List<SerializationProperty<TYPE, Object, ?>> properties = (List<SerializationProperty<TYPE, Object, ?>>) switch (structure)
      {
         case PropertySerializationView<?> propertySerializationStructure -> propertySerializationStructure.getProperties();
         case PropertyView<?> propertyStructure -> propertyStructure.getProperties();
      };

      for (SerializationProperty<TYPE, Object, ?> property : properties)
      {
         Optional<SerializationView<Object, ?>> nested = property.getNestedRead();

         if (nested.isPresent())
         {
            Element element = document.createElement(property.getName());
            parent.appendChild(element);

            serializeInstance(property.getGetter().apply(instance), nested.get(), element, document);
         }
         else if (((boolean) property.getSetting(NAMESPACE, "ATTRIBUTE", false).getValue()))
         {
            parent.setAttribute(property.getName(), render(property.getGetter().apply(instance)));
         }
         else
         {
            Element element = document.createElement(property.getName());
            Text textNode = document.createTextNode(render(property.getGetter().apply(instance)));
            element.appendChild(textNode);
            parent.appendChild(element);
         }
      }
   }

   private static Map<Class<?>, Function<Object, String>> RENDERERS = initRenders();

   private static Map<Class<?>, Function<Object, String>> initRenders()
   {
      Map<Class<?>, Function<Object, String>> result = new HashMap<>();

      addRenderer(result, String.class, s -> s);
      addRenderer(result, Integer.class, Object::toString);
      addRenderer(result, Long.class, Object::toString);

      return result;
   }

   private static <T> void addRenderer(Map<Class<?>, Function<Object, String>> result, Class<T> type, Function<T, String> renderer)
   {
      //noinspection unchecked
      result.put(type, (Function<Object, String>) renderer);
   }

   private static String render(Object o)
   {
      Function<Object, String> renderer = RENDERERS.get(o.getClass());
      if (renderer == null)
      {
         throw new RuntimeException("No renderer found for " + o.getClass());
      }
      return renderer.apply(o);
   }
}
