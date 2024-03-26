package io.determann.serialization.view;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Properties
{
   public static <TYPE, PROPERTY, SELF extends ReadAndWriteProperty<TYPE, PROPERTY, SELF>> ReadAndWriteProperty<TYPE, PROPERTY, SELF>
   readAndWrite(String name,
                Class<PROPERTY> type,
                Function<TYPE, PROPERTY> getter,
                BiConsumer<TYPE, PROPERTY> setter)
   {
      return new PropertyImpl<>(name, type, getter, setter, null, null);
   }

   public static <TYPE, PROPERTY, SELF extends ReadAndWriteProperty<TYPE, PROPERTY, SELF>> ReadAndWriteProperty<TYPE, PROPERTY, SELF>
   readAndWrite(String name,
                Class<PROPERTY> type,
                Function<TYPE, PROPERTY> getter,
                BiConsumer<TYPE, PROPERTY> setter,
                PropertyView<PROPERTY> nested)
   {
      return new PropertyImpl<>(name, type, getter, setter, nested, nested);
   }

   public static <TYPE, PROPERTY, SELF extends SerializationProperty<TYPE, PROPERTY, SELF>> SerializationProperty<TYPE, PROPERTY, SELF>
   serialization(String name,
                 Class<PROPERTY> type,
                 Function<TYPE, PROPERTY> getter)
   {
      return new PropertyImpl(name, type, getter, null, null, null);
   }

   public static <TYPE, PROPERTY, SELF extends SerializationProperty<TYPE, PROPERTY, SELF>> SerializationProperty<TYPE, PROPERTY, SELF>
   serialization(String name,
                 Class<PROPERTY> type,
                 Function<TYPE, PROPERTY> getter,
                 SerializationView<PROPERTY, ?> nested)
   {
      return new PropertyImpl(name, type, getter, null, null, nested);
   }

   public static <TYPE, PROPERTY, SELF extends DeSerializationProperty<TYPE, PROPERTY, SELF>> DeSerializationProperty<TYPE, PROPERTY, SELF>
   deSerialization(String name,
                   Class<PROPERTY> type,
                   BiConsumer<TYPE, PROPERTY> setter)
   {
      return new PropertyImpl(name, type, null, setter, null, null);
   }

   public static <TYPE, PROPERTY, SELF extends DeSerializationProperty<TYPE, PROPERTY, SELF>> DeSerializationProperty<TYPE, PROPERTY, SELF>
   deSerialization(String name,
                   Class<PROPERTY> type,
                   BiConsumer<TYPE, PROPERTY> setter,
                   DeSerializationView<PROPERTY, ?> nested)
   {
      return new PropertyImpl(name, type, null, setter, nested, null);
   }
}
