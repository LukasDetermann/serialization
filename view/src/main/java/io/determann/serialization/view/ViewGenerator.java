package io.determann.serialization.view;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.ShadowProcessor;
import io.determann.shadow.api.property.MutableProperty;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Class;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class ViewGenerator extends ShadowProcessor
{
   private Map<String, SettingsRenderer> settingsRenderer;

   @Override
   public synchronized void init(ProcessingEnvironment processingEnv)
   {
      super.init(processingEnv);

      settingsRenderer = loadRenderer();
   }

   private static Map<String, SettingsRenderer> loadRenderer()
   {
      try
      {
         return ServiceLoader.load(SettingsRenderer.class, SettingsRenderer.class.getClassLoader())
                             .stream()
                             .map(ServiceLoader.Provider::get)
                             .collect(Collectors.toMap(SettingsRenderer::namespace, r -> r));
      }
      catch (ServiceConfigurationError e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void process(AnnotationProcessingContext context) throws Exception
   {
      Annotation renderableSetting = context.getAnnotationOrThrow("io.determann.serialization.view.RenderableSetting");

      for (Class aClass : context.getAnnotatedWith("io.determann.serialization.view.Serializable").classes())
      {
         StringBuilder result = new StringBuilder();
         String qualifiedName = aClass.getQualifiedName() + "View";

         result.append("package ").append(aClass.getPackage().getQualifiedName()).append(";\n\n")
               .append("import ").append(aClass.getQualifiedName()).append(";\n")
               .append("import io.determann.serialization.view.*;\n\n")
               .append("public interface JobView {\n");

         List<String> propertyNames = new ArrayList<>();

         for (MutableProperty property : aClass.getMutableProperties())
         {
            propertyNames.add(property.getName().toUpperCase());

            String type = ((Nameable) property.getType()).getName();

            result.append("\npublic static final ReadAndWriteProperty<")
                  .append(aClass.getName()).append(", ").append(type).append(", ?> ")
                  .append(property.getName().toUpperCase()).append(" = ")
                  .append("Properties.readAndWrite(\"").append(property.getName()).append("\", ")
                  .append(type).append(".class, ")
                  .append(aClass.getName()).append("::").append(property.getGetter().getName()).append(", ")
                  .append(aClass.getName()).append("::").append(property.getSetter().getName()).append(")");

            if (property.getField().isPresent())
            {
               result.append(renderSettings(property.getFieldOrThrow(), renderableSetting));
            }

            result.append(";\n");
         }

         result.append("\npublic static final PropertyView<").append(aClass.getName()).append("> ")
               .append(aClass.getName().toUpperCase()).append("_PROPERTY_VIEW = ")
               .append("Views.property(").append(aClass.getName()).append("::new)")
               .append(propertyNames.stream().map(name -> ".with(" + name + ")").collect(joining()))
               .append(renderSettings(aClass, renderableSetting))
               .append(";\n")
               .append("}");

         context.writeAndCompileSourceFile(qualifiedName, result.toString());
      }
   }

   private String renderSettings(Annotationable annotated, Annotation renderableSetting)
   {
      StringBuilder result = new StringBuilder();

      for (AnnotationUsage usage : annotated.getDirectAnnotationUsages())
      {
         Optional<AnnotationUsage> renderableSettingUsage = usage.getDirectUsageOf(renderableSetting);
         if (renderableSettingUsage.isEmpty())
         {
            continue;
         }
         SettingsRenderer renderer = settingsRenderer.get(renderableSettingUsage.get().getValueOrThrow("namespace").asString());
         if (renderer == null)
         {
            continue;
         }
         Optional<String> setting = renderer.render(usage, annotated);
         if (setting.isEmpty())
         {
            continue;
         }
         result.append(setting.get());
      }
      return result.toString();
   }
}
