package io.determann.serialization.xml;

import io.determann.serialization.view.SettingsRenderer;
import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.shadow.AnnotationUsage;

import java.util.Optional;

public class XmlSettingsRenderer implements SettingsRenderer
{
   @Override
   public String namespace()
   {
      return Xml.NAMESPACE;
   }

   @Override
   public Optional<String> render(AnnotationUsage annotationUsage, Annotationable annotated)
   {
      if (annotationUsage.getQualifiedName().equals(Attribute.class.getName()))
      {
         return Optional.of(".withSetting(io.determann.serialization.xml.Xml.attribute())");
      }
      if (annotationUsage.getQualifiedName().equals(Root.class.getName()))
      {
         return Optional.of(".withSetting(io.determann.serialization.xml.Xml.root(\"" +
                            annotationUsage.getValueOrThrow("value").asString() +
                            "\"))");
      }
      throw new IllegalArgumentException();
   }
}
